pipeline {
    agent any

    tools {
        maven 'Maven 3.8.6'
        jdk 'Java 21'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/najjariheb6/test-api-rest-assured.git'
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean test'
            }
        }
    }

}
