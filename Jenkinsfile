pipeline {
    agent any
    environment {
        DOCKER_COMPOSE_FILE = "docker-compose.prod.yml"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Test Backend') {
            steps {
                dir('barpal-backend') {
                    sh './mvnw test'
                }
            }
        }
        stage('Test Frontend') {
            steps {
                dir('barpal-web') {
                    sh 'npm ci'
                    sh 'npm test -- --watchAll=false'
                }
            }
        }
        stage('Build Docker Images') {
            steps {
                script {
                    sh 'docker compose -f $DOCKER_COMPOSE_FILE build'
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    sh 'docker compose -f $DOCKER_COMPOSE_FILE up -d'
                }
            }
        }
        stage('Cleanup') {
            steps {
                script {
                    sh 'docker system prune -f || true'
                }
            }
        }
    }
    post {
        always {
            echo 'Pipeline finished.'
        }
    }
}
// End of Jenkinsfile