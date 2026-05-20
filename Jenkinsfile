pipeline {
    agent any
    
    tools {
        maven 'Maven-3'
    }
    
    environment {
        NEXUS_CREDS = credentials('nexus-credentials')
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean compile'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
        
        stage('Deploy to Nexus') {
            when {
                expression { currentBuild.result == null || currentBuild.result == 'SUCCESS' }
            }
            steps {
                writeFile file: 'settings.xml', text: """
                    <settings>
                        <servers>
                            <server>
                                <id>nexus-releases</id>
                                <username>${NEXUS_CREDS_USR}</username>
                                <password>${NEXUS_CREDS_PSW}</password>
                            </server>
                        </servers>
                    </settings>
                """
                sh 'mvn deploy -s settings.xml'
            }
        }
    }
    
    post {
        success { echo 'Pipeline réussi ! Artefact dans Nexus' }
        failure { echo 'Pipeline échoué ! Aucun déploiement' }
        always { cleanWs() }
    }
}
