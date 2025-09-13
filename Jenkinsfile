pipeline {
    agent any
    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.prod.yml'
        ENV_FILE = '.env.prod'
        // Set a default registry (override with Jenkins credentials/params)
        DOCKER_REGISTRY = ''
        IMAGE_PREFIX = 'barpal'
        BUILD_TAG = "${env.BUILD_NUMBER}"
        BACKEND_HOST_PORT = '8083'
    }
    options {
        timestamps()
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Prepare Environment') {
            steps {
                script {
                    if (!fileExists(ENV_FILE)) {
                        error "Missing ${ENV_FILE} needed for production compose run"
                    }
                    sh 'echo Using env file: $ENV_FILE'
                }
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    // Build images (backend Dockerfile already skips tests via -DskipTests)
                    sh 'docker compose -f $DOCKER_COMPOSE_FILE --env-file $ENV_FILE build'
                }
            }
        }
        stage('Tag Images') {
            when { expression { return env.DOCKER_REGISTRY?.trim() } }
            steps {
                script {
                    def suffix = "$DOCKER_REGISTRY/$IMAGE_PREFIX"
                    sh """
                    docker tag barpal-backend-prod:latest ${suffix}-backend:${BUILD_TAG}
                    docker tag barpal-web-prod:latest ${suffix}-web:${BUILD_TAG}
                    docker tag barpal-backend-prod:latest ${suffix}-backend:latest
                    docker tag barpal-web-prod:latest ${suffix}-web:latest
                    """
                }
            }
        }
        stage('Push Images') {
            when { expression { return env.DOCKER_REGISTRY?.trim() } }
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-registry-creds', usernameVariable: 'REG_USER', passwordVariable: 'REG_PASS')]) {
                        sh 'echo $REG_PASS | docker login $DOCKER_REGISTRY -u $REG_USER --password-stdin'
                        sh """
                        docker push $DOCKER_REGISTRY/$IMAGE_PREFIX-backend:${BUILD_TAG}
                        docker push $DOCKER_REGISTRY/$IMAGE_PREFIX-web:${BUILD_TAG}
                        docker push $DOCKER_REGISTRY/$IMAGE_PREFIX-backend:latest
                        docker push $DOCKER_REGISTRY/$IMAGE_PREFIX-web:latest
                        """
                    }
                }
            }
        }
        stage('Deploy (Compose Up)') {
            steps {
                script {
                    sh 'docker compose -f $DOCKER_COMPOSE_FILE --env-file $ENV_FILE up -d'
                }
            }
        }
        stage('Post-Deploy Health') {
            steps {
                script {
                    sh 'sleep 12'
                    sh 'curl -f http://localhost:$BACKEND_HOST_PORT/actuator/health || (echo "Backend health endpoint failed" && docker logs barpal-backend-prod || true && exit 1)'
                }
            }
        }
        stage('Cleanup Old Images') {
            steps {
                script {
                    sh 'docker image prune -f || true'
                }
            }
        }
    }
    post {
        success {
            echo 'Deployment succeeded.'
        }
        failure {
            echo 'Deployment failed.'
        }
        always {
            echo 'Pipeline finished.'
        }
    }
}
// End of Jenkinsfile (enhanced)