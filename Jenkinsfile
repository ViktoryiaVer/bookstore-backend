pipeline {
    agent any
  stages {
        stage('Build Docker Image') {
            steps {
              sh 'docker build -f bookstore-app_gradle_build.dockerfile -t my-bookstore-app:$BUILD_ID .'
            }
        }
    }
}