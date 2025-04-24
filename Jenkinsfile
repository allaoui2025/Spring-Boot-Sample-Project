pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'farid2025/springboot-app'
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
                sh '''
                pipx install semgrep || true
                ~/.local/bin/semgrep --config p/owasp-top-ten .
                '''
            }
        }

        stage('🐳 Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE .'
            }
        }

        stage('📤 Push Docker Image to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                    echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                    docker push $DOCKER_IMAGE
                    '''
                }
            }
        }

        stage('🚀 Run Docker Container') {
            steps {
                sh 'docker run -d -p 8080:8080 $DOCKER_IMAGE'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline finished successfully!'
        }
        failure {
            echo '❌ Pipeline failed!'
        }
    }
}

