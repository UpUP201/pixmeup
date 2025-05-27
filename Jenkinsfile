pipeline {
    agent any

    environment {
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        DOCKER_REGISTRY = 'bsh7937'
        SPRINGBOOT_IMAGE_NAME = "${DOCKER_REGISTRY}/pixelro-springboot"
        FASTAPI_IMAGE_NAME = "${DOCKER_REGISTRY}/pixelro-fastapi"
        NGINX_IMAGE_NAME = "${DOCKER_REGISTRY}/pixelro-nginx"

        // GitLab 저장소 URL
        GIT_REPO_URL = 'https://lab.ssafy.com/s12-final/S12P31S201.git'
        // Git 푸시 접근 권한이 있는 Jenkins Credential ID
        GIT_PUSH_CREDENTIALS_ID = 'gitlab-push-credentials'
        // GitLab API 토큰의 Jenkins Credential ID
        GITLAB_API_TOKEN_CRED_ID = 'bsh7937-gitlab-pat'
        // PR을 생성할 대상 브랜치
        TARGET_BRANCH_FOR_PR = 'develop'
        // Jenkins가 생성하는 커밋에 사용될 Git 사용자 정보
        GIT_USER_EMAIL = 'bsh7937@naver.com'
        GIT_USER_NAME = '배성훈'
        // GitLab API URL
        GITLAB_API_URL = 'https://lab.ssafy.com/api/v4'
        // GitLab 프로젝트 ID 
        GITLAB_PROJECT_ID = '997275'

    }

    stages {
        // 공통
        stage('Git Checkout') {
            steps {
                checkout scm
            }
        }

        // 서버 CI
        stage('Build Server JAR') {
            when {
                branch 'server-develop'
            }
            steps {
                dir('server/springboot') {
                    sh '''
                        echo "🛠️ Building Spring Boot JAR..."
                        chmod +x gradlew
                        ./gradlew clean bootJar
                        # 생성된 JAR 파일을 Docker 빌드 컨텍스트 위치로 복사 (Dockerfile 위치에 따라 조정)
                        # 예: cp build/libs/*.jar ../../staging/server-app.jar
                        echo "✅ Spring Boot JAR build complete."
                    '''
                }
            }
            post {
                always {
                    script {
                        mattermostSend(
                            endpoint: 'https://meeting.ssafy.com/hooks/cdhopbqzt3n3ucxucpx731jm5c', // ✅ 하드코딩
                            channel: 'S201-notification',
                            color: currentBuild.currentResult == 'SUCCESS' ? 'good' : 'danger',
                            message: "${currentBuild.currentResult == 'SUCCESS' ? '✅ 서버 JAR 빌드 성공 🐳' : '❌ 서버 JAR 빌드 실패 ❌'}\n작업: ${env.JOB_NAME} #${env.BUILD_NUMBER}\n상세보기: ${env.BUILD_URL}"
                        )
                    }
                }
            }
        }

        stage('Build & Push Server Docker Image') {
            when {
                branch 'server-develop'
            }
            steps {
                script {
                    def specificTag = env.IMAGE_TAG
                    def baseImageName = env.SPRINGBOOT_IMAGE_NAME
                    def fullImageNameSpecific = "${baseImageName}:${specificTag}"

                    echo "📦 Building Spring Boot Docker image: ${fullImageNameSpecific}"
                    sh "docker build -t ${fullImageNameSpecific} -f server/springboot/spring-dockerfile server/springboot"

                    echo " Pushing image ${fullImageNameSpecific} to registry ${env.DOCKER_REGISTRY}..."
                    withCredentials([usernamePassword(credentialsId: 'pixelro-docker-registry', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                        sh "docker push ${fullImageNameSpecific}"
                        sh "docker logout"
                    }
                    echo "✅ Spring Boot Docker image (${fullImageNameSpecific}) push complete."
                }
            }
            post {
                success {
                    script {
                        env.SPRINGBOOT_PUSH_SUCCESS = 'true'
                    }
                }
                always {
                    script {
                        def status = currentBuild.currentResult == 'SUCCESS' ? '✅ 성공' : '❌ 실패'
                        def imageVersion = env.IMAGE_TAG ?: env.BUILD_NUMBER
                        mattermostSend(
                            endpoint: 'https://meeting.ssafy.com/hooks/cdhopbqzt3n3ucxucpx731jm5c',
                            channel: 'S201-notification',
                            color: currentBuild.currentResult == 'SUCCESS' ? 'good' : 'danger',
                            message: "${status} Spring Boot Docker 이미지 푸시\n작업: ${env.JOB_NAME} #${env.BUILD_NUMBER}\n상세: ${env.BUILD_URL}\n버전: ${imageVersion}"
                        )
                    }
                }
            }
        }

        stage('Build & Push FastAPI Docker Image') {
            when {
                branch 'server-develop'
            }
            steps {
                script {
                    def specificTag = env.IMAGE_TAG
                    def baseImageName = env.FASTAPI_IMAGE_NAME
                    def fullImageNameSpecific = "${baseImageName}:${specificTag}"

                    echo "📦 Building FastAPI Docker image: ${fullImageNameSpecific}"
                    sh "docker build -t ${fullImageNameSpecific} -f server/fastapi-app/python-dockerfile server/fastapi-app"

                    echo " Pushing image ${fullImageNameSpecific} to registry ${env.DOCKER_REGISTRY}..."
                    withCredentials([usernamePassword(credentialsId: 'pixelro-docker-registry', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                        sh "docker push ${fullImageNameSpecific}"
                        sh "docker logout"
                    }
                    echo "✅ FastAPI Docker image (${fullImageNameSpecific}) push complete."
                }
            }
            post {
                success {
                    script {
                        env.FASTAPI_PUSH_SUCCESS = 'true'
                    }
                }
                always {
                    script {
                        def status = currentBuild.currentResult == 'SUCCESS' ? '✅ 성공' : '❌ 실패'
                        def imageVersion = env.IMAGE_TAG ?: env.BUILD_NUMBER
                        mattermostSend(
                            endpoint: 'https://meeting.ssafy.com/hooks/cdhopbqzt3n3ucxucpx731jm5c',
                            channel: 'S201-notification',
                            color: currentBuild.currentResult == 'SUCCESS' ? 'good' : 'danger',
                            message: "${status} FastAPI Docker 이미지 빌드/푸시\n작업: ${env.JOB_NAME} #${env.BUILD_NUMBER}\n상세: ${env.BUILD_URL}\n버전: ${imageVersion}"
                        )
                    }
                }
            }
        }

        // 통합 MR 생성 스테이지 (Spring Boot & FastAPI 용)
        stage('Create Server Image Tags Update MR') {
            when {
                branch 'server-develop'
            }
            steps {
                script {
                    def springBootSuccess = (env.SPRINGBOOT_PUSH_SUCCESS == 'true')
                    def fastApiSuccess = (env.FASTAPI_PUSH_SUCCESS == 'true')

                    def newVersion = env.IMAGE_TAG
                    def updatesMade = []
                    def updatedTagKeys = []

                    if (springBootSuccess || fastApiSuccess) {
                        echo "🚀 통합 서버 이미지 버전 업데이트 MR 생성 프로세스를 시작합니다..."
                        dir("pr_workspace_server_combined") {
                            deleteDir()
                            git branch: env.TARGET_BRANCH_FOR_PR, credentialsId: env.GIT_PUSH_CREDENTIALS_ID, url: env.GIT_REPO_URL

                            def featureBranch = "feature/auto-update-server-tags-${newVersion}"
                            sh "git checkout -b ${featureBranch}"

                            if (!fileExists('deploy_versions.env')) {
                                error("${env.TARGET_BRANCH_FOR_PR} 브랜치에서 deploy_versions.env 파일을 찾을 수 없습니다.")
                            }

                            if (springBootSuccess) {
                                def tagKey = 'SPRINGBOOT_IMAGE_TAG'
                                sh "sed -i -E 's/^(${tagKey}=).*/\\1${newVersion}/' deploy_versions.env"
                                echo "📝 deploy_versions.env: ${tagKey}를 ${newVersion}으로 업데이트."
                                updatesMade.add("Spring Boot")
                                updatedTagKeys.add(tagKey)
                            }
                            if (fastApiSuccess) {
                                def tagKey = 'FASTAPI_IMAGE_TAG'
                                sh "sed -i -E 's/^(${tagKey}=).*/\\1${newVersion}/' deploy_versions.env"
                                echo "📝 deploy_versions.env: ${tagKey}를 ${newVersion}으로 업데이트."
                                updatesMade.add("FastAPI")
                                updatedTagKeys.add(tagKey)
                            }

                            sh "git config --global user.email '${env.GIT_USER_EMAIL}'"
                            sh "git config --global user.name '${env.GIT_USER_NAME}'"
                            sh "git add deploy_versions.env"
                            def commitMessage = "feat: Jenkins가 ${updatedTagKeys.join(' 및 ')}를 버전 ${newVersion}으로 자동 업데이트"
                            sh "git commit -m \"${commitMessage}\""

                            // Git Push
                            withCredentials([usernamePassword(credentialsId: env.GIT_PUSH_CREDENTIALS_ID, usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_TOKEN')]) {
                                def encodedUsername = java.net.URLEncoder.encode(GIT_USERNAME, "UTF-8")
                                def authenticatedRepoUrl = env.GIT_REPO_URL.replace("https://", "https://${encodedUsername}:${GIT_TOKEN}@")
                                sh "git push ${authenticatedRepoUrl} HEAD:${featureBranch} --set-upstream"
                            }
                            echo "📤 변경 사항을 ${featureBranch} 브랜치로 푸시했습니다."

                            // GitLab Merge Request 생성
                            withCredentials([gitlabApiToken(credentialsId: env.GITLAB_API_TOKEN_CRED_ID, variable: 'GITLAB_TOKEN')]) {
                                try {
                                    def mrTitleText = "[Server/Deploy] ${updatesMade.join(' 및 ')} 이미지 태그 버전 ${newVersion} 업데이트"

                                    def mrDescriptionBody = updatesMade.collect { serviceName ->
                                        def imageName = (serviceName == 'Spring Boot') ? env.SPRINGBOOT_IMAGE_NAME : env.FASTAPI_IMAGE_NAME
                                        def imageTagKey = (serviceName == 'Spring Boot') ? 'SPRINGBOOT_IMAGE_TAG' : 'FASTAPI_IMAGE_TAG'
                                        "- ${imageName}:${newVersion} 빌드 후 ${imageTagKey}를 ${newVersion}으로 업데이트"
                                    }.join("\n")

                                    def mrDescriptionText = "이 Merge Request는 Jenkins CI 빌드 #${env.BUILD_NUMBER}에서 다음 이미지들의 태그를 자동으로 업데이트하기 위해 생성되었습니다:\n\n${mrDescriptionBody}"
                                    
                                    def jsonData = """
                                        {
                                            "source_branch": "${featureBranch}",
                                            "target_branch": "${env.TARGET_BRANCH_FOR_PR}",
                                            "title": "${mrTitleText.replace('"', '\\"')}",
                                            "description": "${mrDescriptionText.replace('"', '\\"').replace('\n', '\\n')}",
                                            "remove_source_branch": true
                                        }
                                    """
                                    
                                    def mrResponse = sh(script: """
                                        curl -s -X POST \\
                                        -H "PRIVATE-TOKEN: ${GITLAB_TOKEN}" \\
                                        -H "Content-Type: application/json" \\
                                        "${env.GITLAB_API_URL}/projects/${env.GITLAB_PROJECT_ID}/merge_requests" \\
                                        -d '${jsonData}'
                                    """, returnStdout: true).trim()
                                    
                                    echo "GitLab API 응답: ${mrResponse}"
                                    
                                    def mrUrl = sh(script: "echo '${mrResponse}' | grep -o '\"web_url\":\"[^\"]*\"' | sed 's/\"web_url\":\"\\(.*\\)\"/\\1/'", returnStdout: true).trim()
                                    
                                    if (mrUrl) {
                                        echo "✅ 통합 MR 생성됨: ${mrUrl}"
                                        mattermostSend(
                                            endpoint: 'https://meeting.ssafy.com/hooks/cdhopbqzt3n3ucxucpx731jm5c',
                                            channel: 'S201-notification',
                                            color: 'good',
                                            message: "✅ 통합 MR 생성됨: ${updatesMade.join(' 및 ')} 버전 ${newVersion} 업데이트\n브랜치: ${featureBranch} -> ${env.TARGET_BRANCH_FOR_PR}\nMR: ${mrUrl}\n작업: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
                                        )
                                    } else {
                                        echo "⚠️ MR URL을 추출하지 못했습니다. API 응답: ${mrResponse}"
                                        mattermostSend(
                                            endpoint: 'https://meeting.ssafy.com/hooks/cdhopbqzt3n3ucxucpx731jm5c',
                                            channel: 'S201-notification',
                                            color: 'danger',
                                            message: "❌ 통합 MR 생성 실패 (URL 추출 오류): ${updatesMade.join(' 및 ')} 버전 ${newVersion} 업데이트\nAPI 응답: ${mrResponse}\n작업: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
                                        )
                                    }
                                } catch (Exception e) {
                                    echo "⚠️ 통합 MR 생성 실패: ${e.getMessage()}"
                                    mattermostSend(
                                        endpoint: 'https://meeting.ssafy.com/hooks/cdhopbqzt3n3ucxucpx731jm5c',
                                        channel: 'S201-notification',
                                        color: 'danger',
                                        message: "❌ 통합 MR 생성 실패: ${updatesMade.join(' 및 ')} 버전 ${newVersion} 업데이트\n상세: ${e.getMessage()}\n작업: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
                                    )
                                }
                            }
                        }
                    }
                }
            }
            post {
                always {
                    script {
                        def finalStatus = (currentBuild.result == 'SUCCESS' || currentBuild.result == null) ? "성공" : "실패"
                        mattermostSend(
                            endpoint: 'https://meeting.ssafy.com/hooks/cdhopbqzt3n3ucxucpx731jm5c',
                            channel: 'S201-notification',
                            color: (finalStatus == "성공") ? 'good' : 'danger',
                            message: "${(finalStatus == '성공') ? '✅' : '❌'} 서버 이미지 태그 업데이트 MR 생성 시도 ${finalStatus}\n작업: ${env.JOB_NAME} #${env.BUILD_NUMBER}\n상세: ${env.BUILD_URL}"
                        )
                    }
                }
            }
        }

        // Nginx CI
        stage('Build & Push Nginx Docker Image') {
            when {
                branch 'web-develop'
            }
            steps {
                script {
                    def specificTag = env.IMAGE_TAG
                    def baseImageName = env.NGINX_IMAGE_NAME

                    def fullImageNameSpecific = "${baseImageName}:${specificTag}"

                    echo "📦 Building Nginx Docker image: ${fullImageNameSpecific}"
                    sh "docker build -t ${fullImageNameSpecific} -f web/nginx-dockerfile web"

                    echo " Pushing image ${fullImageNameSpecific} to registry ${env.DOCKER_REGISTRY}..."
                    withCredentials([usernamePassword(credentialsId: 'pixelro-docker-registry', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                        sh "docker push ${fullImageNameSpecific}"
                        sh "docker logout"
                    }
                    echo "✅ Nginx Docker image (${fullImageNameSpecific}) push complete."
                }
            }
            post {
                success {
                    script {
                        echo "🚀 Nginx 이미지 버전 업데이트를 위한 MR 생성 프로세스를 시작합니다..."
                        def serviceImageTagKey = 'NGINX_IMAGE_TAG'
                        def newVersion = env.IMAGE_TAG

                        // Git 작업을 위한 임시 디렉토리 사용 (충돌 방지)
                        dir("pr_workspace_nginx") {
                            // 1. 이전 시도 정리 및 PR 대상 브랜치 체크아웃
                            deleteDir()
                            git branch: env.TARGET_BRANCH_FOR_PR, credentialsId: env.GIT_PUSH_CREDENTIALS_ID, url: env.GIT_REPO_URL

                            // 2. 새로운 기능 브랜치 생성
                            def featureBranch = "feature/auto-update-${serviceImageTagKey.toLowerCase().replace('_','-')}-${newVersion}"
                            sh "git checkout -b ${featureBranch}"

                            // 3. deploy_versions.env 파일 수정
                            if (!fileExists('deploy_versions.env')) {
                                error("${env.TARGET_BRANCH_FOR_PR} 브랜치 체크아웃 후 작업 공간에서 deploy_versions.env 파일을 찾을 수 없습니다. 이 파일이 저장소 루트에 해당 브랜치에 존재하는지 확인하세요.")
                            }
                            // sed 명령어 사용 (Linux 버전)
                            sh "sed -i -E 's/^(${serviceImageTagKey}=).*/\\1${newVersion}/' deploy_versions.env"
                            echo "📝 deploy_versions.env 파일의 ${serviceImageTagKey}를 ${newVersion}으로 업데이트했습니다."

                            // 4. 변경 사항 커밋 및 푸시
                            sh "git config --global user.email '${env.GIT_USER_EMAIL}'"
                            sh "git config --global user.name '${env.GIT_USER_NAME}'"
                            sh "git add deploy_versions.env"
                            sh "git commit -m 'feat: Jenkins에 의해 ${serviceImageTagKey}를 버전 ${newVersion}으로 업데이트'"
                            withCredentials([usernamePassword(credentialsId: env.GIT_PUSH_CREDENTIALS_ID, usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_TOKEN')]) {
                                def encodedUsername = java.net.URLEncoder.encode(GIT_USERNAME, "UTF-8")
                                def authenticatedRepoUrl = env.GIT_REPO_URL.replace("https://", "https://${encodedUsername}:${GIT_TOKEN}@")
                                sh "git push ${authenticatedRepoUrl} HEAD:${featureBranch} --set-upstream"
                            }
                            echo "📤 변경 사항을 ${featureBranch} 브랜치로 푸시했습니다."

                            // 5. GitLab Merge Request 생성
                            withCredentials([gitlabApiToken(credentialsId: env.GITLAB_API_TOKEN_CRED_ID, variable: 'GITLAB_TOKEN')]) {
                                try {
                                    def mrTitleText = "[Web/Deploy] ${serviceImageTagKey}를 ${newVersion}으로 업데이트"
                                    def mrDescriptionText = "이 Merge Request는 Jenkins가 ${env.NGINX_IMAGE_NAME}:${newVersion} 빌드 후 deploy_versions.env 파일의 ${serviceImageTagKey}를 버전 ${newVersion}으로 업데이트하기 위해 자동으로 생성했습니다."

                                    def jsonDataNginx = """
                                        {
                                            "source_branch": "${featureBranch}",
                                            "target_branch": "${env.TARGET_BRANCH_FOR_PR}",
                                            "title": "${mrTitleText.replace('"', '\\"')}",
                                            "description": "${mrDescriptionText.replace('"', '\\"').replace('\n', '\\n')}",
                                            "remove_source_branch": true
                                        }
                                    """
                                    
                                    def mrResponse = sh(script: """
                                        curl -s -X POST \\
                                        -H "PRIVATE-TOKEN: ${GITLAB_TOKEN}" \\
                                        -H "Content-Type: application/json" \\
                                        "${env.GITLAB_API_URL}/projects/${env.GITLAB_PROJECT_ID}/merge_requests" \\
                                        -d '${jsonDataNginx}'
                                    """, returnStdout: true).trim()
                                    
                                    echo "GitLab API 응답: ${mrResponse}"

                                    def mrUrl = sh(script: "echo '${mrResponse}' | grep -o '\"web_url\":\"[^\"]*\"' | sed 's/\"web_url\":\"\\(.*\\)\"/\\1/'", returnStdout: true).trim()
                                    
                                    if (mrUrl) {
                                        echo "✅ 통합 MR 생성됨: ${mrUrl}"
                                        mattermostSend(
                                            endpoint: 'https://meeting.ssafy.com/hooks/cdhopbqzt3n3ucxucpx731jm5c',
                                            channel: 'S201-notification',
                                            color: 'good',
                                            message: "✅ 통합 MR 생성됨: ${serviceImageTagKey} 버전 ${newVersion} 업데이트\n브랜치: ${featureBranch} -> ${env.TARGET_BRANCH_FOR_PR}\nMR: ${mrUrl}\n작업: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
                                        )
                                    } else {
                                        echo "⚠️ MR URL을 추출하지 못했습니다. API 응답: ${mrResponse}"
                                        mattermostSend(
                                            endpoint: 'https://meeting.ssafy.com/hooks/cdhopbqzt3n3ucxucpx731jm5c',
                                            channel: 'S201-notification',
                                            color: 'danger',
                                            message: "❌ 통합 MR 생성 실패 (URL 추출 오류): ${serviceImageTagKey} 버전 ${newVersion} 업데이트\nAPI 응답: ${mrResponse}\n작업: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
                                        )
                                    }
                                } catch (Exception e) {
                                    echo "⚠️ 통합 MR 생성 실패: ${e.getMessage()}"
                                    mattermostSend(
                                        endpoint: 'https://meeting.ssafy.com/hooks/cdhopbqzt3n3ucxucpx731jm5c',
                                        channel: 'S201-notification',
                                        color: 'danger',
                                        message: "❌ 통합 MR 생성 실패: ${serviceImageTagKey} 버전 ${newVersion} 업데이트\n상세: ${e.getMessage()}\n작업: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
                                    )
                                }
                            }
                        }
                    }
                }
                always {
                    script {
                        def status = currentBuild.currentResult == 'SUCCESS' ? '✅ 성공' : '❌ 실패'
                        def imageVersion = env.IMAGE_TAG ?: env.BUILD_NUMBER
                        mattermostSend(
                            endpoint: 'https://meeting.ssafy.com/hooks/cdhopbqzt3n3ucxucpx731jm5c', // ✅ 하드코딩
                            channel: 'S201-notification',
                            color: currentBuild.currentResult == 'SUCCESS' ? 'good' : 'danger',
                            message: "${status} Nginx Docker 이미지 빌드/푸시\n작업: ${env.JOB_NAME} #${env.BUILD_NUMBER}\n상세: ${env.BUILD_URL}\n버전: ${imageVersion}"
                        )
                    }
                }
            }
        }

        // 배포
        stage('Deploy Services') {
            when { branch 'develop' }
            steps {
                script {
                    if (!fileExists('deploy_versions.env')) {
                        error('deploy_versions.env 파일이 작업공간 루트에 존재하지 않습니다.')
                    }
                    def props = readProperties file: 'deploy_versions.env'
                    env.SPRINGBOOT_TAG_TO_DEPLOY = props.SPRINGBOOT_IMAGE_TAG
                    env.FASTAPI_TAG_TO_DEPLOY = props.FASTAPI_IMAGE_TAG
                    env.NGINX_TAG_TO_DEPLOY = props.NGINX_IMAGE_TAG

                    if (!env.SPRINGBOOT_TAG_TO_DEPLOY || !env.FASTAPI_TAG_TO_DEPLOY || !env.NGINX_TAG_TO_DEPLOY) {
                        error("deploy_versions.env 파일에서 이미지 태그를 찾을 수 없거나 일부 태그가 누락되었습니다. (SPRINGBOOT_IMAGE_TAG, FASTAPI_IMAGE_TAG, NGINX_IMAGE_TAG 확인 필요)")
                    }
                }
                withCredentials([file(credentialsId: 'pixelro-env', variable: 'ENV_FILE_SECRET_PATH')]) {
                    script {
                        def springbootTagValue = env.SPRINGBOOT_TAG_TO_DEPLOY
                        def fastapiTagValue = env.FASTAPI_TAG_TO_DEPLOY
                        def nginxTagValue = env.NGINX_TAG_TO_DEPLOY

                        def springbootImageNameValue = env.SPRINGBOOT_IMAGE_NAME
                        def fastapiImageNameValue = env.FASTAPI_IMAGE_NAME
                        def nginxImageNameValue = env.NGINX_IMAGE_NAME ?: "${env.DOCKER_REGISTRY}/pixelro-client"

                        sh """ 
                            echo "🚀 Deploying services using specific versions from deploy_versions.env..."
                            echo "   Spring Boot Version: ${springbootTagValue}"
                            echo "   FastAPI Version: ${fastapiTagValue}"
                            echo "   Nginx Version: ${nginxTagValue}"

                            # Export할 이미지 이름 변수들
                            export SPRINGBOOT_IMAGE_NAME=${springbootImageNameValue}
                            export FASTAPI_IMAGE_NAME=${fastapiImageNameValue}
                            export NGINX_IMAGE_NAME=${nginxImageNameValue}
                            
                            # Export할 이미지 태그 변수들
                            export SPRINGBOOT_IMAGE_TAG=${springbootTagValue}
                            export FASTAPI_IMAGE_TAG=${fastapiTagValue}
                            export NGINX_IMAGE_TAG=${nginxTagValue}

                            # 특정 버전 이미지 가져오기 - 쉘 변수(\$)를 사용
                            echo "   Pulling specific images..."
                            docker pull \$SPRINGBOOT_IMAGE_NAME:\$SPRINGBOOT_IMAGE_TAG 
                            docker pull \$FASTAPI_IMAGE_NAME:\$FASTAPI_IMAGE_TAG
                            docker pull \$NGINX_IMAGE_NAME:\$NGINX_IMAGE_TAG

                            # ⬇️ 추가: docker-compose up 전에 기존 컨테이너/네트워크 정리
                            echo "   Stopping and removing existing services (if any)..."
                            docker-compose -f docker-compose.yml --env-file ${ENV_FILE_SECRET_PATH} down

                            # 서비스 시작/업데이트
                            echo "   Starting/Updating services with docker-compose..."
                            docker-compose -f docker-compose.yml --env-file ${ENV_FILE_SECRET_PATH} up -d --no-build --force-recreate --remove-orphans

                            # 리소스 정리
                            echo "   Cleaning up unused docker resources..."
                            docker image prune -f

                            echo "✅ Deployment complete!"
                        """
                    }
                }
            }
            post {
                always {
                    script {
                         def status = currentBuild.currentResult == 'SUCCESS' ? '✅ 성공' : '❌ 실패'
                        mattermostSend(
                            endpoint: 'https://meeting.ssafy.com/hooks/cdhopbqzt3n3ucxucpx731jm5c',
                            channel: 'S201-notification',
                            color: currentBuild.currentResult == 'SUCCESS' ? 'good' : 'danger',
                            message: "${status} 서비스 배포 (${env.BRANCH_NAME} 브랜치)\n작업: ${env.JOB_NAME} #${env.BUILD_NUMBER}\n상세: ${env.BUILD_URL}"
                        )
                    }
                }
            }
        }
    }
}