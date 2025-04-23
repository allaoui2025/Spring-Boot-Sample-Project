pipeline {
    agent any

    environment {
        IMAGE_NAME = 'farid2025/spring-boot-sample-app:latest'
    }

    tools {
        jdk 'jdk11'
        maven 'maven3'
    }

    stages {

        stage('📥 Clone Repository') {
            steps {
                checkout scm
            }
        }

        stage('📦 Build with Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('🐳 Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('📤 Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $IMAGE_NAME
                    '''
                }
            }
        }

        stage('🔍 Trivy Scan') {
            steps {
                sh '''
                    curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin
                    trivy image $IMAGE_NAME || true
                '''
            }
        }

        stage('🛡️ Semgrep Scan') {
            steps {
                sh '''
                    pip install --break-system-packages semgrep || true
                    semgrep scan --config=auto --error || true
                '''
            }
        }
    }
}
