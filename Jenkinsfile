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
  }


//   stage('Performance Test') {
//     steps {
//       sh 'ngrinder_agent run'
//       sh 'ngrinder_controller test'
//     }
//   }
}