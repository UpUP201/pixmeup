# PixmeUp
<div align="center">
<img src="/uploads/f30a08a4a2dc1576f13b1b7551767110/image.png" alt="메인이미지" width="90%">
</div>

###  **현대인의 눈 건강을 위한 AI 기반 예측 플랫폼**
> **PixmeUp**은 사용자의 눈 사진과 간단한 검사를 바탕으로 **AMD(황반변성)** 등 주요 안질환 위험도를 예측하고,사용자 맞춤형 **눈 운동 가이드**와 **질병 요약 리포트**를 제공하는 플랫폼입니다.  
전문 장비 없이도 집에서 손쉽게 눈 상태를 관리할 수 있도록 지원합니다.

<br/>

## 🚀 주요 기능

### 1. QR을 이용한 키오스크의 검사 결과 저장
### 2. 패스키를 활용한 간편 로그인
### 3. AI 예측 기능
  - 눈 사진 기반 안질환 예측 (YOLOv8 기반)
  - 다회 검사 기록을 활용한 시력 예측 (선형 회귀 기반)
  - 문진 결과 기반 AMD 위험도 예측 (AREDS Risk Model 적용)
  - 누적 검사 통합 리포트 및 질병별 요약
### 4. 눈 운동 가이드
  - GIF, TTS를 활용한 시청각 가이드 제공공
### 5. 신뢰도 기반 pseudo-label 재학습 파이프라인 자동화

<br/>

## ✨ 주요 화면 소개

### 1. 검사 결과 저장 및 리포트
<div align="center">
<img src="/uploads/66a229314c1040dcf2a17ffd606ea2f4/1번리포트.gif" alt="1번리포트" width="25%">
</div>
<br/>

### 2. AI 예측(AMD 예측 / 눈 질환 예측)
<div align="center">
  <img src="/uploads/79315b5a4941ca44928bf7e7ffdc9045/2번AMD.gif" width="25%" style="display:inline-block; margin-right: 10%;" />
  <span style="display:inline-block; width:10%;"></span>
  <img src="/uploads/f9da5e6f774bbb5fda08f2bef82c8847/3번눈이미지진단.gif" width="25%" />
</div>
<br/>


### 3. 눈 운동 
<div align="center">
  <img src="/uploads/f3956bef122416de8dfdfef32a78bea7/4번눈운동.gif" width="25%" style="display:inline-block; margin-right: 10%;" />
  <span style="display:inline-block; width:10%;"></span>
  <img src="/uploads/545d4d01c92b3019fa3c7409324391c0/5번한단계.gif" width="25%" />
</div>
<br/>

<br/>

## 🔍 시스템 아키텍처

<img src="./images/architecture.png" alt="PixmeUp Architecture" width="600"/>

<br/>

---

## 🛠️ 기술 스택

### 💻 프론트엔드

| 분류            | 기술                                                                                                                                                                                                                                                                                                                                                                                                                            | 설명                                                                     |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------ |
| 언어/프레임워크 | ![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=flat&logo=typescript&logoColor=white) ![React](https://img.shields.io/badge/React-61DAFB?style=flat&logo=react&logoColor=black)<br/>![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white) ![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84?style=flat&logo=androidstudio&logoColor=white) | 타입 안정성과 컴포넌트 기반 UI<br>시력 검사 기능 탑재 안드로이드 앱 개발 |
| 상태 관리       | ![Zustand](https://img.shields.io/badge/Zustand-000000?style=flat&logo=zustand&logoColor=white)                                                                                                                                                                                                                                                                                                                                 | 전역 상태 관리                                                           |
| 서버 상태       | ![TanStack Query](https://img.shields.io/badge/TanStack%20Query-FF4154?style=flat&logo=reactquery&logoColor=white)                                                                                                                                                                                                                                                                                                              | API 요청/응답 캐싱 및 상태관리                                           |
| 스타일링        | ![Tailwind CSS](https://img.shields.io/badge/TailwindCSS-06B6D4?style=flat&logo=tailwindcss&logoColor=white) ![shadcn/ui](https://img.shields.io/badge/shadcn/ui-000000?style=flat)                                                                                                                                                                                                                                          | 유틸리티 기반 CSS 및 UI 컴포넌트                                         |



### ⚙️ 백엔드 / AI

| 분류            | 기술                                                                                                                                                                                                                      | 설명                           |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------ |
| 언어/프레임워크 | ![Java](https://img.shields.io/badge/Java%2017-007396?style=flat&logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=springboot&logoColor=white)                  | 백엔드 REST API 구현           |
| 데이터베이스    | ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat&logo=redis&logoColor=white)                                    | 사용자 정보 및 세션 캐시 저장  |
| 보안/인증       | ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat&logo=springsecurity&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-000000?style=flat&logo=jsonwebtokens&logoColor=white) | JWT 기반 인증/인가 처리        |
| ORM             | ![JPA](https://img.shields.io/badge/JPA-007396?style=flat)                                                                                                                                                                | JPA 활용 데이터 접근           |
| AI 서버 연동    | ![Python](https://img.shields.io/badge/Python-3776AB?style=flat&logo=python&logoColor=white) ![FastAPI](https://img.shields.io/badge/FastAPI-009688?style=flat&logo=fastapi&logoColor=white)                           | FastAPI 기반 AI 진단 서버 연동 |
| AI 모델         | ![YOLOv8](https://img.shields.io/badge/YOLOv8-FF1493?style=flat)                                                                                                                                                          |

### ☁️ 공통 인프라 및 협업 도구

| 분류          | 기술                                                                                                                                                                                                                                                                                   | 설명                                    |
| ------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------- |
| 파일 저장     | ![Amazon S3](https://img.shields.io/badge/AWS%20S3-569A31?style=flat&logo=amazonaws&logoColor=white)                                                                                                                                                                                   | 사용자 이미지, 라벨, 모델 가중치 저장   |
| 배포 & 인프라 | ![AWS](https://img.shields.io/badge/AWS-232F3E?style=flat&logo=amazonaws&logoColor=white) ![NGINX](https://img.shields.io/badge/Nginx-009639?style=flat&logo=nginx&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white) | EC2, RDS, S3, 리버스 프록시, 컨테이너화 |
| CI/CD         | ![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=flat&logo=jenkins&logoColor=white)                                                                                                                                                                                        | Jenkins 기반 빌드/배포 자동화           |
| 협업/관리     | ![GitLab](https://img.shields.io/badge/GitLab-FC6D26?style=flat&logo=gitlab&logoColor=white) ![Jira](https://img.shields.io/badge/Jira-0052CC?style=flat&logoColor=white)                                                                                                           | 형상관리 및 이슈 관리                   |
| 문서화        | ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger&logoColor=black)                                                                                                                                                                                        |

<br/>

## 📁 주요 디렉토리 구조

```
backend/
├── src/main/java/com/corp/pixelro
│   ├── check/              # 검사 관련 도메인
│   ├── survey/             # 문진 관련
│   ├── external/           # FastAPI 통신
│   ├── global/             # 공통 설정, 예외처리
│   └── user/               # 사용자 및 인증
fastapi-app/
├── app/
│   ├── api/                # /predict API
│   ├── services/           # 예측 로직
│   ├── models/             # 모델 및 가중치
│   └── schemas/            # 요청/응답 정의
```

<br/>

## 📘 API 문서
> ### [Swagger UI](http://localhost:8088/api/v1/swagger-ui.html)

<br/>


## 👨‍👩‍👧‍👦 팀원 소개

| 이름   | 역할                   | GitHub                                                 |
| ------ | ---------------------- | ------------------------------------------------------ |
| 김종명 | 팀장 / 프론트엔드 개발 | [github.com/jump6746](https://github.com/jump6746)     |
| 차윤영 | 백엔드 팀장            | [github.com/yuncof](https://github.com/yuncof)         |
| 배성훈 | 백엔드 개발            | [github.com/baehoonbae](https://github.com/baehoonbae) |
| 이동욱 | 백엔드 개발            | [github.com/2Ludy](https://github.com/2Ludy)           |
| 오은지 | 프론트엔드 개발        | [github.com/oille12](https://github.com/oille12)       |
| 윤이영 | 프론트엔드 개발        | [github.com/y20ng](https://github.com/y20ng)           |

---


## 📄 라이선스

```
본 프로젝트는 교육 과정의 일환으로 제작된 비상업적 프로젝트입니다.
상업적 목적의 사용을 금지합니다.

```

© 2025 PixmeUp. All Rights Reserved.


