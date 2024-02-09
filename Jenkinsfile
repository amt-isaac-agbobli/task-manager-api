def appname = ' '
def deploy_group = ''
def s3_bucket = ''
def s3_filename = ''

//Slack Notification Integration
def gitName = env.GIT_BRANCH
def jobName = env.JOB_NAME
def branchName = env.BRANCH_NAME
def main_branch = ['main', 'develop']

// Environments Declaration
environment {
  jobName = env.JOB_NAME
  branchName = env.BRANCH_NAME
}

// Successful Build
def buildSuccess = [
  [text: "Task Manager API Build Successful on ${branchName}",
  fallback: "Task Manager API Build Successful on ${branchName}",
  color: "#00FF00"
  ]
]

// Failed Build
def buildError = [
  [text: "Task Manager API Build Failed on ${branchName}",
  fallback: "Task Manager API Build Failed on ${branchName}",
  color: "#FF0000"
  ]
]

pipeline {
  agent any

  stages {
    stage('Build') {
      steps {
        sh 'chmod +x ./mvnw'
        sh './mvnw wrapper:wrapper'
      }
    }

    stage('Run Tests') {
      steps {
        sh 'chmod +x ./mvnw'
        sh './mvnw test'
      }
    }


    stage('Prepare to Deploy') {

      steps {
        withAWS(region:'eu-west-1',credentials:'aws_cred') {
          script {
            def gitsha = sh(script: 'git log -n1 --format=format:"%H"', returnStdout: true)
            s3_filename = "${s3_filename}-${gitsha}"
            sh """
              aws deploy push \
              --application-name ${appname} \
              --description "This is a revision for the ${appname}-${gitsha}" \
              --ignore-hidden-files \
              --s3-location s3://${s3_bucket}/${s3_filename}.zip \
              --source .
            """
          }
        }
      }
    }

    stage('Deploy to Development') {
    //   when {
    //     anyOf {
    //         branch 'main'
    //         branch 'develop'
    //     }
    // }
      steps {
        withAWS(region:'eu-west-1',credentials:'aws_cred') {
          script {
            sh """
              aws deploy create-deployment \
              --application-name ${appname} \
              --deployment-config-name CodeDeployDefault.OneAtATime \
              --deployment-group-name ${deploy_group} \
              --file-exists-behavior OVERWRITE \
              --s3-location bucket=${s3_bucket},key=${s3_filename}.zip,bundleType=zip
            """
          }
        }
      }
    }

    stage('Clean WS') {
      steps {
        cleanWs()
      }
    }
  }
}