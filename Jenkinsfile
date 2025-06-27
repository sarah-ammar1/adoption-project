pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "sarah1407/adoption-project"
        NEXUS_URL = "http://nexus:8081"
        SONAR_SERVER = 'sonarqube'
        PROJECT_KEY = 'adoption-project'
        GIT_REPO = "https://github.com/sarah-ammar1/adoption-project.git "
        SONAR_TOKEN = credentials('SONARQUBE_TOKEN')

        // Use the exact credential IDs from Jenkins
        DOCKER_HUB_CRED_ID = "28462d94-1f6d-4e48-8753-5553a46d3673"  // Docker Hub
        NEXUS_CRED_ID = "e12281ed-f59d-4052-a032-32c5d29f32ba"        // Nexus

        // SONAR_TOKEN_ID = "sqa_3bce9215175745a13c6d85c637785f206ccd93e2"       // SonarQube token
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
            chmod +x ./mvnw
            ./mvnw clean package -Dhttps.protocols=TLSv1.2
        '''
            }
        }


stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'SONARQUBE_TOKEN', variable: 'SONAR_LOGIN')]) {
                    sh '''
                        ./mvnw sonar:sonar \
                          -Dsonar.host.url=http://sonarqube:9000 \
                          -Dsonar.login=${SONAR_LOGIN} \
                          -Dsonar.verbose=true  # Enable debug logs
                    '''
                }
            }
        }
        stage('Upload Artifact to Nexus') {
            steps {
                echo "📦 Uploading JAR to Nexus"
                
                // Use Nexus credentials
                withCredentials([
                    usernamePassword(
                        credentialsId: "${NEXUS_CRED_ID}",
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASSWORD'
                    )
                ]) {
                    sh '''
                        NEXUS_HOST="http://nexus:8081"
                        REPO_NAME="maven-releases"
                        FILE_PATH="target/adoption-Project-0.0.1-SNAPSHOT.jar"

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
                
                // Use Docker Hub credentials
                withDockerRegistry([credentialsId: "${DOCKER_HUB_CRED_ID}", url: ""]) {
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
    }
    failure {
        echo "❌ Pipeline failed! Check logs for details."
    }
    always {
        echo "🧹 Cleaning up workspace..."
        deleteDir()
    }
}
}