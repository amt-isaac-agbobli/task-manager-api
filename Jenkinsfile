def appname = ''
def deploy_group = ''
def deploy_group_prod = ''
def s3_bucket = ''
def s3_filename = ''

//Slack Notification Integration
def gitName = env.GIT_BRANCH
def jobName = env.JOB_NAME
def branchName = env.BRANCH_NAME
def main_branch = ['main']

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
tools {
    maven 'maven'
    jdk 'jdk8'
}

stages {
    // stage('Build') {
    //     when {
    //         anyOf {
    //             branch 'main'
    //             branch 'develop'
    //         }
    //     }
    //     steps {
    //         sh 'mvn clean install'
    //     }
    // }
    stage('Build') {
        steps {
            sh './mvnw wrapper:wrapper'
        }
    }

    stage('Run Tests') {
        when {
            anyOf {
                branch 'main'
            }
        }
        steps {
            sh 'mvn test'
        }
    }
}

     stage('Prepare to Deploy') {
         when {
             anyOf {
                 branch 'main';
             }
         }

       steps {
         withAWS(region:'eu-west-1',credentials:'aws-cred') {
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
         when {
             branch 'main'
         }
       steps {
         withAWS(region:'eu-west-1',credentials:'aws-cred') {
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
    // stage('Deploy To Production') {
    //   when {
    //     branch 'staging'
    //   }
    //   steps {
    //     withAWS(region:'eu-west-1',credentials:'aws-cred') {
    //       script {
    //         sh """
    //             aws deploy create-deployment \
    //             --application-name ${appname} \
    //             --deployment-config-name CodeDeployDefault.OneAtATime \
    //             --deployment-group-name ${deploy_group_prod} \
    //             --file-exists-behavior OVERWRITE \
    //             --s3-location bucket=${s3_bucket},key=${s3_filename}.zip,bundleType=zip
    //           """
    //       }
    //     }
    //   }
    // }
    stage('Clean WS') {
      steps {
        cleanWs()
      	}
   	}
 }
//  post {
//     always {
//       echo 'One way or another, I have finished'
//       cleanWs()
//     }
//     success {
//       script {
//         if (BRANCH_NAME in main_branch) {
//             slackSend(channel:"task-manager", attachments: buildSuccess)
//           }
//       }
//         echo 'I passed successfully'
//     }
//     unstable {
//       echo 'I am unstable :/'
//     }
//     failure {
//     script {
//       if (BRANCH_NAME in main_branch) {
//           slackSend(channel:"task-manager", attachments: buildError)
//           }
//     }
//         echo 'I have failed'
//     }
//     changed {
//       echo 'Things were different before...'
//     	}
//   }
}