package com.corp.pixelro.external.service;

import com.corp.pixelro.external.dto.FaissIndexResponse;
import com.corp.pixelro.external.dto.UploadResponse;
import com.corp.pixelro.global.error.code.ErrorCode;
import com.corp.pixelro.global.error.exception.S3Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class S3Service {

    private final S3Presigner s3Presigner;
    private final String bucket;
    private final S3Client s3Client;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public S3Service(
            @Value("${cloud.aws.credentials.access-key}") String accessKey,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${cloud.aws.s3.bucket}") String bucket
    ) {
        this.bucket = bucket;

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3Presigner = S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        this.s3Client = S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public UploadResponse uploadImage(Long userId, String fileName, String contentType){
        String key;

        if (fileName.endsWith(".txt")) {
            key = "user_data/labels/" + fileName;
        } else {
            // 확장자 관계 없이 image이면 pseudo/images로 간주
            key = "user_data/images/" + fileName;
        }

        String presignedUrl = generatePresignedPutUrl(key, contentType);

        return new UploadResponse(
                presignedUrl,
                key
        );
    }

    public String generatePresignedPutUrl(String key, String contentType) {
        if(key == null || contentType == null) {
            throw new S3Exception(ErrorCode.S3_INPUT_NOT_VAILD);
        }

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))  // 10분 유효
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(presignRequest);

        String url = presignedPutObjectRequest.url().toString();
        return url.replace("&amp;", "&");
    }

    public String generatePresignedGetUrl(String key) {
        if(key == null) {
            throw new S3Exception(ErrorCode.S3_INPUT_NOT_VAILD);
        }
        // ✅ Redis key에 prefix 추가
        String redisKey = "S3Presign::" + key;

        // ✅ 1. Redis에서 기존 presigned URL 조회
        String cachedUrl = (String) redisTemplate.opsForValue().get(redisKey);
        if (cachedUrl != null) {
            return cachedUrl;
        }

        // ✅ 2. Redis에 없다면 presigned URL 새로 생성
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // 10분 유효
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
        String url = presignedGetObjectRequest.url().toString().replace("&amp;", "&");

        // ✅ 3. Redis에 캐싱 (TTL = presigned URL, presigned TTL - 5% 룰을 적용 하여 600*0.95 = 570s)
        redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(570));

        return url;
    }

    public String makeS3Key(Long userId, String originalFileName) {
        String extension = "";
        int idx = originalFileName.lastIndexOf('.');
        if (idx > 0) {
            extension = originalFileName.substring(idx);
        }
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "user_data/" + userId + "-" + now + extension;
    }

    public void putRawBytesToPresignedUrl(String presignedUrl, byte[] fileBytes, String contentType) {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            HttpEntity<byte[]> entity = new HttpEntity<>(fileBytes, headers);

            log.info("📤 S3 raw bytes put: presignedUrl={}, fileBytes={}, contentType={}", presignedUrl, fileBytes.length, contentType);
            new RestTemplate().exchange(presignedUrl, HttpMethod.PUT, entity, String.class);
        }catch(Exception e){
            log.error("S3 raw bytes put error: {}", e.getMessage());
            throw e;
        }
    }

    public String downloadLatestModelFile() {
        // 1. 최신 모델 키 찾기
        ListObjectsV2Response response = s3Client.listObjectsV2(ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix("models/")
                .build());

        List<S3Object> objects = response.contents().stream()
                .filter(obj -> obj.key().endsWith(".pt"))
                .sorted(Comparator.comparing(S3Object::lastModified).reversed())
                .toList();

        if (objects.isEmpty()) {
            throw new IllegalStateException("S3에 모델 파일이 없습니다.");
        }

        String latestKey = objects.get(0).key(); // 최신 모델

        // 2. 서버 경로로 저장
        String localPath = "/home/ubuntu/app/models/weights/best.pt"; // FastAPI 기준 경로
        File targetFile = new File(localPath);
        targetFile.getParentFile().mkdirs(); // 디렉토리 없으면 생성

        s3Client.getObject(GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(latestKey)
                        .build(),
                ResponseTransformer.toFile(targetFile)
        );

        return latestKey;
    }

    public FaissIndexResponse downloadFaiss() {
        String faissUrl = generatePresignedGetUrl("llm/index.faiss");
        String pklUrl = generatePresignedGetUrl("llm/index.pkl");
        return FaissIndexResponse.builder()
            .faissUrl(faissUrl)
            .pklUrl(pklUrl)
            .build();
    }

}