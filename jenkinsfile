pipeline {
    agent any

    environment {
        IMAGE_NAME = "farid2025/devops-app"
        CONTAINER_NAME = "devops-app"
        HOST_PORT = "8081" // استعملنا 8081 باش نتفاداو التعارض
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

        stage('🐳 Build Docker Image') {
            steps {
                sh "docker build -t $IMAGE_NAME ."
            }
        }

        stage('📤 Push Docker Image to DockerHub') {
            steps {
                echo "🚀 Pushing Docker image to DockerHub..."
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
                echo "🧹 Cleaning old container if exists, then running new container on port ${HOST_PORT}"
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
