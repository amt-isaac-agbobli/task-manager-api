
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

    stage('Clean WS') {
      steps {
        cleanWs()
      }
    }
  }
}