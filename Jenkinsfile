pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'farid2025'
        IMAGE_NAME = 'springboot-app'
    }

    stages {
        stage('📥 Clone Repository') {
            steps {
                git 'https://github.com/allaoui2025/Spring-Boot-Sample-Project'
            }
        }

        stage('🔧 Build Maven Project') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('🔍 Semgrep (OWASP Rules)') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh '''
                    pipx install semgrep || true
                    ~/.local/bin/semgrep --config p/owasp-top-ten .
                    '''
                }
            }
        }

        stage('🐳 Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKERHUB_USERNAME/$IMAGE_NAME .'
            }
        }

        stage('📤 Push Docker Image to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh '''
                    echo "$PASSWORD" | docker login -u "$USERNAME" --password-stdin
                    docker push $DOCKERHUB_USERNAME/$IMAGE_NAME
                    '''
                }
            }
        }

        stage('🚀 Run Docker Container') {
            steps {
                sh '''
                docker run -d -p 8080:8080 --name springboot-container $DOCKERHUB_USERNAME/$IMAGE_NAME || true
                '''
            }
        }
    }

    post {
        failure {
            echo "❌ Pipeline failed!"
        }
        success {
            echo "✅ Pipeline completed successfully!"
        }
    }
}
