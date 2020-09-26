pipeline {
    agent any
    tools {
        jdk '11'
        maven 'latest'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }
    stages {
        stage('Build') {
            steps {
                configFileProvider(
                    [configFile(fileId: 'jfrog', variable: 'MAVEN_SETTINGS')]) {
                    sh 'mvn -B -s $MAVEN_SETTINGS -DskipTests clean deploy'
                }
            }
        }
    }
}