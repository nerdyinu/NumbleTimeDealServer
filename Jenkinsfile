pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        sshagent(['git']) {
          checkout([
            $class: 'GitSCM',
            branches: [[name: '*/deploy']],
            userRemoteConfigs: [[
              url: 'git@github.com:inudev5/NumbleTimeDealServer.git',
              credentialsId: 'github-credentials'
            ]]
          ])
        }
      }
    }
    // Other stages here
      stage('Build') {
        steps {
          sh './gradlew clean build'
        }
      }
    stage('Test') {
      steps {
        sh './gradlew test'
      }
    }
    stage("Package"){
      steps{
        sh './gradlew bootJar'
      }
    }


    stage('Build Docker Image') {
      steps {
        script {
          docker.build("inust33/myapp:${env.BUILD_NUMBER}")
          docker.withRegistry('https://registry.hub.docker.com/v2', 'docker_credentials') {
            docker.image("inust33/myapp:${env.BUILD_NUMBER}").push()
          }
        }
      }
    }

        stage('Deploy Docker Container') {
          steps {
            script {
              sh 'docker stop myapp || true'
              sh 'docker rm myapp || true'

              sh "docker run -p 7070:8080 -d --name myapp --network timedeal inust33/myapp:${env.BUILD_NUMBER}"
            }
          }
        }
//         stage("run pinpoint container"){
//             steps{
//                 script{
//                     sh 'docker compose down'
//                     sh 'docker compose up -d'
//                 }
//             }
//         }
//         stage("run ngrinder"){
//           steps {
//            script {
//              sh "docker compose up && docker compose rm -fsv"
//            }
//          }
//         }

 }
}