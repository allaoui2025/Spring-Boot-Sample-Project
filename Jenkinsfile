 pipeline {
    agent any

    environment {
        IMAGE_NAME = "farid2025/springboot-app:latest"
    }

    stages {
        stage('📦 Build Maven Project') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('🐳 Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('📤 Push Docker Image to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $IMAGE_NAME
                    '''
                }
            }
        }

        stage('🔐 Trivy Scan (Docker Image Vulnerabilities)') {
            steps {
                sh '''
                    sudo apt update -y
                    sudo apt install -y wget gnupg lsb-release
                    curl -fsSL https://aquasecurity.github.io/trivy-repo/deb/public.key | sudo gpg --dearmor -o /usr/share/keyrings/trivy-archive-keyring.gpg
                    echo "deb [signed-by=/usr/share/keyrings/trivy-archive-keyring.gpg] https://aquasecurity.github.io/trivy-repo/deb $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/trivy.list > /dev/null
                    sudo apt update -y
                    sudo apt install -y trivy
                    trivy image $IMAGE_NAME || true
                '''
            }
        }

        stage('🔍 Semgrep Code Scan') {
            steps {
                sh '''
                    pip install pipx --break-system-packages || true
                    pipx install semgrep || true
                    ~/.local/bin/semgrep scan --config auto --json || true
                '''
            }
        }
    }
}

