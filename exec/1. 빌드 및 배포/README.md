# 📦 서울 기업연계 S2반 1팀(S201) 프로젝트 - 빌드 및 배포 매뉴얼

## 1. 개발 및 실행 환경

### ✅ 사용 기술 및 버전

| 항목        | 내용                                                |
| ----------- | --------------------------------------------------- |
| JVM         | Java Liberica 17 (빌드 시 사용)                     |
| IDE         | IntelliJ IDEA 2024.3.2 (Ultimate) + Gradle 플러그인 |
| Node.js     | 22.12.0                                             |
| Android SDK | API Level 33                                        |
| 웹서버      | NGINX 1.18.0 (리버스 프록시 사용, 8088 포트 포워딩) |
| WAS         | Spring Boot Embedded Tomcat (포트 8088)             |
| 운영체제    | Ubuntu 20.04 LTS (EC2 환경)                         |
| 빌드 도구   | Gradle 8.5 (Wrapper 포함)                           |

## 2. 환경 변수 설정

### 📂 환경 변수 위치

- `.env` 파일: 프로젝트 루트에 위치
- 또는 OS 환경 변수로 설정 (`~/.bashrc`, IntelliJ Run Config 등)

### 🌿 환경 변수 전체 목록

```

\---- Spring Boot ----

# 🔧 File Storage

FILE_STORAGE_PATH=

# 🛢️ Database (MySQL)

MYSQL_URL=
MYSQL_ROOT_USER=
MYSQL_ROOT_PASSWORD=
MYSQL_DATABASE=
MYSQL_USERNAME=
MYSQL_PASSWORD=

# 🔑 JWT

JWT_SECRET=
JWT_ACCESS_TOKEN_EXPIRATION=3600000
JWT_REFRESH_TOKEN_EXPIRATION=604800000
JWT_TEMPORARY_TOKEN_EXPIRATION=300000

# 🔌 Redis

REDIS_HOST=
REDIS_PORT=6379
REDIS_PASSWORD=
REDIS_TTL=3600000

# 🖥️ Server

SERVER_PORT=8088

# 📋 Logging Levels

LOG_LEVEL_ROOT=INFO
LOG_LEVEL_SPRING_WEB=DEBUG
LOG_LEVEL_APP=DEBUG
LOG_LEVEL_MYBATIS=TRACE

# 🌱 Spring Profile

SPRING_PROFILES_ACTIVE=dev

# WebAuthn

WEBAUTHN_RP_ID=
WEBAUTHN_RP_NAME=
WEBAUTHN_RP_ORIGIN=

# Cookie Domain

APP_COOKIE_DOMAIN=

# Cool SMS

COOLSMS_API_KEY=
COOLSMS_API_SECRET=
COOLSMS_SENDER_PHONE=

# Docker Image

SPRINGBOOT_IMAGE_NAME=
FASTAPI_IMAGE_NAME=
NGINX_IMAGE_NAME=

# FastAPI 연동

FASTAPI_BASE_URL=
FASTAPI_BASE_URL_TEST=

# AWS

AWS_ACCESS_KEY_ID=
AWS_SECRET_ACCESS_KEY=
AWS_S3_BUCKET=
AWS_REGION=

# Grafana

GF_SECURITY_ADMIN_USER=
GF_SECURITY_ADMIN_PASSWORD=

# Prometheus

PROMETHEUS_EXTERNAL_URL=

\---- FastAPI ----

# MongoDB

MONGO_URI=
MONGO_URI_TEST=
MONGO_DB=
MONGO_DB_TEST=
MONGO_INITDB_ROOT_USERNAME=
MONGO_INITDB_ROOT_PASSWORD=

# FastAPI 설정

APP_ENV=dev
APP_PORT=8000

# Roboflow

ROBOFLOW_API_KEY=

# Gemini

GEMINI_API_KEY=

# Spring 연동 주소

SPRING_API_URL=

# PYTHONPATH

PYTHONPATH=.

# LLM / OpenAI

OPENAI_API_KEY=

```

### 💡 적용 방법

- **IntelliJ**: `Run Configuration > Environment` 탭에서 추가
- **리눅스**: `.bashrc` 또는 `.env`에 작성 후 `source ~/.bashrc`로 반영

---

## 3. 배포 시 유의사항

- `application.yml` 파일 내에서 `default`, `test` 등의 프로파일 분기 설정 사용
- `.env` 파일은 반드시 `.gitignore`에 등록해 외부 유출 방지
- EC2 배포 시 HTTPS 인증서는 `certbot`으로 Let’s Encrypt 적용
- NGINX 설정을 통해 80/443 포트를 내부 8088 포트로 리버스 프록시 설정 필요
- `./gradlew build` 명령어로 빌드 가능 (Gradle Wrapper 포함)
- Spring 로그는 Logback 설정으로 파일 출력됨 → 경로 및 파일 권한 확인
- 외부 API(CoolSMS, OpenAI 등) 사용 시 반드시 환경 변수 설정 필요

---

## 4. 📁 주요 설정 파일

### 📁 `application.yml`

- **위치**: `src/main/resources/application.yml`
- **포함 내용**:

  | 항목                     | 설명                                                                                                       |
  | ------------------------ | ---------------------------------------------------------------------------------------------------------- |
  | `server.port`            | 서버 실행 포트 설정 (`8088`) — `SERVER_PORT`로 설정 가능                                                   |
  | `spring.profiles.active` | 실행 프로파일 지정 (`default`, `test`, `dev` 등) — `SPRING_PROFILES_ACTIVE`로 설정                         |
  | `spring.datasource`      | MySQL 데이터베이스 연결 정보 — `MYSQL_URL`, `MYSQL_USERNAME`, `MYSQL_PASSWORD` 사용                        |
  | `spring.jpa`             | Hibernate 설정 (`ddl-auto`, SQL 출력 등)                                                                   |
  | `spring.data.redis`      | Redis 연결 설정 — `REDIS_HOST`, `REDIS_PORT`, `REDIS_PASSWORD`                                             |
  | `jwt`                    | JWT 관련 키 및 만료시간 설정 — `JWT_SECRET`, `JWT_ACCESS_TOKEN_EXPIRATION` 등                              |
  | `mail`                   | 이메일 인증에 사용할 SMTP 서버 설정 (`host`, `port`, `username`, `password`)                               |
  | `coolsms`                | 문자 전송을 위한 API 키 설정 — `COOLSMS_API_KEY`, `COOLSMS_API_SECRET`                                     |
  | `recaptcha`              | Google reCAPTCHA 키 설정 (`site`, `secret`)                                                                |
  | `springdoc`, `swagger`   | Swagger 문서 설정 (API 자동 문서화)                                                                        |
  | `websocket`              | WebSocket 관련 설정 (허용 origin 등)                                                                       |
  | `webauthn`               | WebAuthn 인증 설정 — `WEBAUTHN_RP_ID`, `WEBAUTHN_RP_ORIGIN` 등                                             |
  | `monitoring`             | Grafana/Prometheus 등의 외부 모니터링 도구 연동 설정                                                       |
  | `cloud.aws`              | S3 업로드를 위한 AWS 설정 — `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION`, `AWS_S3_BUCKET` 등 |
  | `frontend`               | 프론트 도메인 설정 (CORS, 쿠키 도메인 등) — `APP_COOKIE_DOMAIN`                                            |

---

### 📁 `.env`

- **위치**: 프로젝트 루트 (백엔드와 FastAPI 공통 사용)
- **포함 내용**:

  #### Spring Boot 관련

  - DB 설정: `MYSQL_URL`, `MYSQL_USERNAME`, `MYSQL_PASSWORD`
  - Redis 설정: `REDIS_HOST`, `REDIS_PORT`, `REDIS_PASSWORD`
  - JWT 키 및 토큰 만료 설정: `JWT_SECRET`, `JWT_ACCESS_TOKEN_EXPIRATION`, 등
  - 로그 레벨: `LOG_LEVEL_ROOT`, `LOG_LEVEL_APP` 등
  - WebAuthn 설정: `WEBAUTHN_RP_ID`, `WEBAUTHN_RP_NAME`, `WEBAUTHN_RP_ORIGIN`
  - 외부 API 키: `COOLSMS_API_KEY`, `COOLSMS_API_SECRET`, `GOOGLE_RECAPTCHA_SECRET`, `OPENAI_API_KEY`, `GEMINI_API_KEY` 등
  - AWS S3 업로드 정보: `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION`, `AWS_S3_BUCKET`
  - 프론트엔드 도메인/쿠키 설정: `APP_COOKIE_DOMAIN`
  - Spring + FastAPI 도커 이미지 이름: `SPRINGBOOT_IMAGE_NAME`, `FASTAPI_IMAGE_NAME`, `NGINX_IMAGE_NAME`
  - FastAPI 연동: `FASTAPI_BASE_URL`, `FASTAPI_BASE_URL_TEST`

  #### FastAPI 관련

  - MongoDB 설정: `MONGO_URI`, `MONGO_DB`, `MONGO_INITDB_ROOT_USERNAME`, `MONGO_INITDB_ROOT_PASSWORD`
  - AI/LLM 연동: `ROBOFLOW_API_KEY`, `GEMINI_API_KEY`, `OPENAI_API_KEY`
  - 환경 구분: `APP_ENV=dev`, `APP_PORT=8000`
  - Python 경로 지정: `PYTHONPATH=.`

- **주의사항**:
  - `.env` 파일은 민감한 정보(API Key, 비밀번호 등)를 포함하므로 Git에 절대 커밋되지 않도록 `.gitignore`에 반드시 포함해야 합니다.
  - 운영 서버 전용 파일이며, 로컬 테스트 시 `.env` 또는 OS 환경 변수로 설정해 적용합니다.

---
