pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "your-dockerhub-username/adoption-app"
        NEXUS_URL = "http://nexus:8081"
        SONAR_SERVER = 'sonarqube'
        PROJECT_KEY = 'adoption-project'
    }

    stages {
        stage('Clone Repo') {
            steps {
                git branch: 'main', url: 'https://github.com/sarah-ammar1/adoption-project.git' 
            }
        }

        stage('Build & Test') {
            steps {
                sh './mvnw clean package'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(installationName: "${SONAR_SERVER}") {
                    sh './mvnw sonar:sonar'
                }
            }
        }

        stage('Upload to Nexus') {
            steps {
                nexusPublisher(
                    nexusInstanceId: 'nexus',
                    nexusRepositoryId: 'maven-releases',
                    packages: [
                        [mavenAssetList: [
                            [classifier: '', extension: '.jar', filePath: 'target/adoption-project-0.0.1-SNAPSHOT.jar']
                        ]]
                    ]
                )
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE}:latest .'
            }
        }

        stage('Push to DockerHub') {
            steps {
                withDockerRegistry([credentialsId: "docker-hub-credentials", url: ""]) {
                    sh 'docker push ${DOCKER_IMAGE}:latest'
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh 'docker-compose down'
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        success {
            echo "✅ Deployment successful!"
        }
        failure {
            echo "❌ Build failed!"
        }
    }
}