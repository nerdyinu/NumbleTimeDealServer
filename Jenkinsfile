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
              credentialsId: 'b60d7587-b545-4277-92db-d1ccefa19b0a'
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
          docker.build("NumbleTimeDealServer-timedeal-server-1.0.0:${env.BUILD_NUMBER}")
          docker.withRegistry('https://registry.example.com', 'docker-credentials') {
            docker.image("NumbleTimeDealServer-timedeal-server-1.0.0:${env.BUILD_NUMBER}").push()
          }
        }
      }
    }

    stage('Deploy Docker Container') {
      steps {
        script {
          sh 'docker run -p 8080:8080 -d NumbleTimeDealServer-timedeal-server-1.0.0:${env.BUILD_NUMBER}'
        }
      }
    }

//   stage('Performance Test') {
//     steps {
//       sh 'ngrinder_agent run'
//       sh 'ngrinder_controller test'
//     }
//   }
 }
}