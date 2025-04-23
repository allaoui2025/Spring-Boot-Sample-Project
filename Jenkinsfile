pipeline {
    agent any

    environment {
        IMAGE_NAME = "farid2025/devops-app"
        CONTAINER_NAME = "devops-app"
        HOST_PORT = "8081"
        CONTAINER_PORT = "8080"
    }

    stages {
        stage('📥 Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/allaoui2025/DevOps.git'
            }
        }

        stage('🔧 Build Maven Project') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('🧠 Semgrep Scan (Code Analysis)') {
            steps {
                echo "🔍 Running Semgrep scan..."
                sh '''
                    pipx install semgrep || true
                    ~/.local/bin/semgrep --config auto .
                '''
            }
        }

        stage('🐳 Build Docker Image') {
            steps {
                sh "docker build -t $IMAGE_NAME ."
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

        stage('📤 Push Docker Image to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker push $IMAGE_NAME
                    '''
                }
            }
        }

        stage('🚀 Run Docker Container') {
            steps {
                sh '''
                    docker rm -f $CONTAINER_NAME || true
                    docker run -d --name $CONTAINER_NAME -p $HOST_PORT:$CONTAINER_PORT $IMAGE_NAME
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline finished successfully!'
        }
        failure {
            echo '❌ Pipeline failed.'
        }
    }
}

     
