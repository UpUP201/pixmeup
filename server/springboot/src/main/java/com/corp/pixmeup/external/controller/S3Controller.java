package com.corp.pixmeup.external.controller;

import com.corp.pixmeup.external.dto.FaissIndexResponse;
import com.corp.pixmeup.external.dto.UploadResponse;
import com.corp.pixmeup.external.service.S3Service;
import com.corp.pixmeup.global.response.GlobalResponse;
import com.corp.pixmeup.global.vo.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/v1/s3/upload-url")
    public ResponseEntity<GlobalResponse<UploadResponse>> getPresignedUploadUrl(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @RequestParam String fileName,
            @RequestParam String contentType) {
        UploadResponse response = s3Service.uploadImage(userDetail.getUserId(), fileName, contentType);

        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

    @PostMapping("/v2/s3/upload-url")
    public ResponseEntity<GlobalResponse<UploadResponse>> getFastApiFiles(
            @RequestParam Long userId,
            @RequestParam String fileName,
            @RequestParam String contentType) {
        UploadResponse response = s3Service.uploadImage(userId, fileName, contentType);

        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

    @PostMapping("/v2/s3/download-model")
    public ResponseEntity<GlobalResponse<String>> downloadLatestModelToServer() {
        String key = s3Service.downloadLatestModelFile();
        return ResponseEntity.ok().body(GlobalResponse.success(key));
    }

    @PostMapping("/v2/s3/download-faiss")
    public ResponseEntity<GlobalResponse<FaissIndexResponse>> downloadFaiss() {
        FaissIndexResponse response = s3Service.downloadFaiss();
        return ResponseEntity.ok().body(GlobalResponse.success(response));
    }
}