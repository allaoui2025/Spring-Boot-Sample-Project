pipeline {
    agent any

    environment {
        IMAGE_NAME = 'farid2025/spring-boot-app:latest'
    }

    stages {
        stage('📥 Clone Repository') {
            steps {
                echo 'Cloning repository...'
                git 'https://github.com/allaoui2025/Spring-Boot-Sample-Project'
            }
        }

        stage('📦 Build with Maven') {
            steps {
                echo 'Building project with Maven...'
                sh 'mvn clean install'
            }
        }

        stage('🐳 Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('📤 Push Docker Image to DockerHub') {
            steps {
                echo 'Pushing Docker image to DockerHub...'
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $IMAGE_NAME
                    '''
                }
            }
        }

        stage('🔎 Semgrep Scan (Code Security)') {
            steps {
                echo 'Running Semgrep scan...'
                sh '''
                    pip install pipx || true
                    pipx install semgrep || true
                    ~/.local/bin/semgrep scan --config auto .
                '''
            }
        }

        stage('🔐 Trivy Scan (Docker Image Vulnerabilities)') {
            steps {
                echo 'Running Trivy scan on Docker image...'
                sh '''
                    sudo apt install -y wget apt-transport-https gnupg lsb-release
                    curl -fsSL https://aquasecurity.github.io/trivy-repo/deb/public.key | sudo gpg --dearmor -o /usr/share/keyrings/trivy-archive-keyring.gpg
                    echo "deb [signed-by=/usr/share/keyrings/trivy-archive-keyring.gpg] https://aquasecurity.github.io/trivy-repo/deb $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/trivy.list
                    sudo apt update
                    sudo apt install -y trivy
                    trivy image $IMAGE_NAME || true
                '''
            }
        }

        stage('🚀 Run Docker Container') {
            steps {
                echo 'Running Docker container...'
                sh 'docker run -d -p 8080:8080 $IMAGE_NAME'
            }
        }
    }
}
     
