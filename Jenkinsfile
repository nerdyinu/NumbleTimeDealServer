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
              credentialsId: 'Jenkins-ssh'
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
  }


//   stage('Performance Test') {
//     steps {
//       sh 'ngrinder_agent run'
//       sh 'ngrinder_controller test'
//     }
//   }
}