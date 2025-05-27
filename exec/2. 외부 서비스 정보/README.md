# 📦 서울 기업연계 S2반 1팀(S201) 프로젝트 - 빌드 및 배포 매뉴얼

## 외부 서비스 연동 정보

### 1. CoolSMS

- **목적**: 회원가입 및 본인 인증 시 SMS 전송
- **사용 API**: CoolSMS REST API
- **필요 정보**:
  - API Key
  - API Secret
  - 발신자 번호 (사전 등록 필요)
- **요금**:
  - 1건당 약 20원
  - 최소 10,000원 단위 충전
- **비고**:
  - 한국 휴대폰 번호만 지원
  - 초당 발송량 제한 존재
- **환경 변수 예시**:
  ```env
  COOLSMS_API_KEY=
  COOLSMS_API_SECRET=
  COOLSMS_SENDER_PHONE=
  ```

---

### 2. AWS S3

- **목적**: 이미지 및 파일 업로드/다운로드
- **사용 API**: AWS SDK (Amazon S3)
- **필요 정보**:

  - AWS Access Key ID
  - AWS Secret Access Key
  - S3 Bucket 이름
  - Region

- **비고**:

  - 업로드 시 Presigned URL 생성 방식 사용
  - 다운로드 URL은 Redis에 TTL 기반으로 캐싱

- **환경 변수 예시**:

  ```env
  AWS_ACCESS_KEY_ID=
  AWS_SECRET_ACCESS_KEY=
  AWS_S3_BUCKET=
  AWS_REGION=
  ```

---

### 4. OpenAI

- **목적**: GPT 기반 텍스트 생성 (AI 설명, 코멘트 등)
- **사용 API**: OpenAI GPT-4 API
- **필요 정보**:

  - OpenAI API Key

- **비고**:

  - FastAPI에서 벡터 검색 기반 RAG 시스템과 연동
  - 프롬프트는 사용자 입력 및 검사 결과 기반으로 커스터마이징

- **환경 변수 예시**:

  ```env
  OPENAI_API_KEY=
  ```

---

### 5. Gemini (Google Generative AI)

- **목적**: 시력 검사 및 문진 기반 AI 코멘트 생성
- **사용 API**: Gemini Pro API
- **필요 정보**:

  - Gemini API Key

- **비고**:

  - FastAPI 서버에서 사용
  - 상태 값(`good`, `normal`, `bad`) 기반 프롬프트 구성

- **환경 변수 예시**:

  ```env
  GEMINI_API_KEY=
  ```

---

### 6. Roboflow

- **목적**: YOLO 기반 이미지 예측 및 시각적 이상 감지
- **사용 API**: Roboflow Hosted Model API
- **필요 정보**:

  - Roboflow API Key

- **비고**:

  - 이미지 URL 또는 base64 방식 지원
  - 모델은 FastAPI 내에서 YOLO 로딩 후 실행

- **환경 변수 예시**:

  ```env
  ROBOFLOW_API_KEY=
  ```

---

### 7. Grafana / Prometheus

- **목적**: 서버 모니터링 및 메트릭 수집
- **사용 도구**: Grafana, Prometheus
- **필요 정보**:

  - Grafana Admin 계정 정보
  - Prometheus 외부 접근 URL

- **비고**:

  - 백엔드 서버 및 Docker Compose로 연결
  - FastAPI, Spring 모듈 상태 모니터링

- **환경 변수 예시**:

  ```env
  GF_SECURITY_ADMIN_USER=
  GF_SECURITY_ADMIN_PASSWORD=
  PROMETHEUS_EXTERNAL_URL=
  ```

---
