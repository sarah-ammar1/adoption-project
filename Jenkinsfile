pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "sarah1407/adoption-project"
        NEXUS_URL = "http://nexus:8081"
        SONAR_SERVER = 'sonarqube'
        PROJECT_KEY = 'adoption-project'
        GIT_REPO = "https://github.com/sarah-ammar1/adoption-project.git "
        DOCKER_HUB_CRED = credentials('docker-hub-credentials') // Jenkins credential ID for Docker Hub
        NEXUS_CRED = credentials('nexus-credentials') // Jenkins credential ID for Nexus
    }

    stages {
        stage('Clone Repository') {
            steps {
                echo "🚀 Cloning repository from ${GIT_REPO}"
                git branch: 'main', url: "${GIT_REPO}"
            }
        }

        stage('Build Application') {
            steps {
                echo "🔨 Building Maven project"
                sh '''
                    ./mvnw clean package
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "📊 Running SonarQube analysis"
                withSonarQubeEnv(installationName: "${SONAR_SERVER}") {
                    sh '''
                        ./mvnw sonar:sonar \
                          -Dsonar.projectKey=${PROJECT_KEY} \
                          -Dsonar.host.url=${NEXUS_URL}/sonarqube \
                          -Dsonar.login=${SONAR_TOKEN}
                    '''
                }
            }
        }

        stage('Upload Artifact to Nexus') {
            steps {
                echo "📦 Uploading JAR to Nexus"
                withCredentials([
                    usernamePassword(
                        credentialsId: 'nexus-credentials',
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASSWORD'
                    )
                ]) {
                    sh '''
                        NEXUS_HOST="http://nexus:8081"
                        REPO_NAME="maven-releases"
                        FILE_PATH="target/adoption-project-0.0.1-SNAPSHOT.jar"

                        curl -u ${NEXUS_USER}:${NEXUS_PASSWORD} -X POST "${NEXUS_HOST}/service/rest/v1/components?repository=${REPO_NAME}" \
                          -H "Content-Type: multipart/form-data" \
                          -F "maven2.groupId=com.example" \
                          -F "maven2.artifactId=adoption-project" \
                          -F "maven2.version=1.0.0" \
                          -F "maven2.asset1.extension=jar" \
                          -F "maven2.asset1.file=@${FILE_PATH}"
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "🐋 Building Docker image: ${DOCKER_IMAGE}:latest"
                sh 'docker build -t ${DOCKER_IMAGE}:latest .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo "🚢 Pushing Docker image to Docker Hub"
                withDockerRegistry([credentialsId: "docker-hub-credentials", url: ""]) {
                    sh 'docker push ${DOCKER_IMAGE}:latest'
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo "🔄 Deploying application using docker-compose"
                sh '''
                    docker-compose down || true
                    docker-compose up -d
                '''
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline succeeded! Application deployed."
            slackSend channel: '#devops-cd', color: 'good', message: "✅ Deployment successful!"
        }
        failure {
            echo "❌ Pipeline failed! Check logs for details."
            slackSend channel: '#devops-cd', color: 'danger', message: "❌ Deployment failed!"
        }
        always {
            echo "🧹 Cleaning up workspace..."
            cleanWs()
        }
    }
}