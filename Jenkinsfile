pipeline {
  agent any
  environment {
      MY_IP = 'http://121.136.121.121'
      NGRINDER_CONTROLLER_URL = 'http://121.136.121.121:8080'
      GIT_SSH_COMMAND = 'ssh -i /var/jenkins_home/.ssh/id_rsa -o StrictHostKeyChecking=no'
  }

  stages {
   stage('Checkout') {
     steps {
       git(
         url: 'git@github.com:inudev5/NumbleTimeDealServer.git',
         branch: 'deploy',
         credentialsId: 'github-credentials'
       )
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
      stage("authenticate"){
        steps{
            script{
                sh """
                curl --user admin:admin ${NGRINDER_CONTROLLER_URL}/perftest/api
                """
            }
        }
    }

      stage('Get Test List') {
          steps {
              script {
                  def response = sh(returnStdout: true, script: '''
                  curl -X GET -H "Content-Type: application/json" \
                  "${NGRINDER_CONTROLLER_URL}/perftest/api?page=0"
                  ''').trim()
                  def jsonResponse = readJSON text: response
                  env.TEST_IDS = jsonResponse.content.id.join(',')
              }
          }
      }

      stage('Run Tests') {
          steps {
              script {
                  def testIDs = env.TEST_IDS.tokenize(',')

                  testIDs.each { testID ->
                      // Start the nGrinder test using REST API
                      sh '''
                      curl -X PUT -H "Content-Type: application/json" \
                      "${NGRINDER_CONTROLLER_URL}/perftest/api/${testID}/start"
                      '''

                      // Poll the nGrinder test status using REST API
                      // and wait for the test to finish
                      def testFinished = false
                      while (!testFinished) {
                          def response = sh(returnStdout: true, script: '''
                          curl -X GET -H "Content-Type: application/json" \
                          "${NGRINDER_CONTROLLER_URL}/perftest/api/${testID}"
                          ''').trim()

                          def jsonResponse = readJSON text: response
                          if (jsonResponse.status in ['C', 'E', 'F']) {
                              testFinished = true
                          } else {
                              sleep(time: 30, unit: 'SECONDS')
                          }
                      }
                  }
              }
          }
        }

 }
}