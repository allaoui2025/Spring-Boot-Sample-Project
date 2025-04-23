pipeline {
    agent any

    environment {
        IMAGE_NAME = 'farid2025/spring-boot-sample-app:latest'
    }

    tools {
        jdk 'jdk11' // تأكد من أن jdk11 متسجل في Jenkins
        maven 'maven3' // تأكد من أن maven3 متسجل أيضا
    }

    stages {

        stage('🔁 Clone Repository') {
            steps {
                echo '📥 Cloning repository...'
                checkout scm
            }
        }

        stage('📦 Build Maven Project') {
            steps {
                echo '🔧 Building with Maven...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('🐳 Build Docker Image') {
            steps {
                echo '🐋 Building Docker image...'
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('📤 Push Docker Image to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $IMAGE_NAME
                    '''
                }
            }
        }

        stage('🔐 Trivy Scan (Docker Image Vulnerabilities)') {
            steps {
                echo '🔎 Running Trivy scan on Docker image...'
                sh '''
                    apt-get update && apt-get install -y wget dpkg
                    wget -q https://github.com/aquasecurity/trivy/releases/latest/download/trivy_0.51.1_Linux-64bit.deb
                    dpkg -i trivy_0.51.1_Linux-64bit.deb || true
                    trivy image $IMAGE_NAME || true
                '''
            }
        }

        stage('🧪 Semgrep Scan (Code Security)') {
            steps {
                echo '🔍 Running Semgrep scan...'
                sh '''
                    pipx install semgrep || true
                    semgrep --config=auto --error || true
                '''
            }
        }
    }
}
