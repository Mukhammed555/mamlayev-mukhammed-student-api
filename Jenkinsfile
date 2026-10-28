// =====================================================================
//  Declarative Jenkins pipeline - mamlayev-mukhammed-student-api
//  Author : Mukhammed Mamlayev
//  Job    : Mamlayev_Mukhammed_Student_API
//  Stages : Checkout -> Build -> Test -> Docker Build -> Verify
//  Any failing "sh" step marks the build FAILED and the remaining stages
//  are skipped, so a broken build or a failing test stops the pipeline.
// =====================================================================
pipeline {
    agent any

    // JDK configured in Manage Jenkins -> Tools -> JDK installations
    tools {
        jdk 'jdk-21'
    }

    // Automation: check GitHub for new commits every ~2 minutes.
    // (A GitHub webhook can be used instead when Jenkins has a public URL.)
    triggers {
        pollSCM('H/2 * * * *')
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '20'))
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
    }

    environment {
        STUDENT_NAME = 'Mukhammed Mamlayev'
        IMAGE_NAME   = 'mamlayev-mukhammed-student-api'
        IMAGE_TAG    = 'v1.0-mamlayev-mukhammed'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "===== Pipeline of student: ${env.STUDENT_NAME} ====="
                echo "Job: ${env.JOB_NAME}  Build: #${env.BUILD_NUMBER}"
                checkout scm
                sh 'git log -1 --pretty=format:"Commit %h by %an: %s"; echo'
            }
        }

        stage('Build') {
            steps {
                echo "Building the Spring Boot application of ${env.STUDENT_NAME} with Gradle"
                sh 'chmod +x gradlew'
                sh './gradlew --no-daemon clean bootJar -x test'
                sh 'ls -lh build/libs/'
            }
        }

        stage('Test') {
            steps {
                echo "Running automated tests for ${env.STUDENT_NAME}"
                sh './gradlew --no-daemon test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker image ${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                sh '''
                    docker build \
                      --label "built-by=Mukhammed Mamlayev" \
                      --label "jenkins-build=${BUILD_NUMBER}" \
                      -t ${IMAGE_NAME}:${IMAGE_TAG} \
                      -t ${IMAGE_NAME}:build-${BUILD_NUMBER} .
                '''
            }
        }

        stage('Verify') {
            steps {
                sh '''
                    if docker image inspect ${IMAGE_NAME}:${IMAGE_TAG} > /dev/null 2>&1; then
                        echo "VERIFY OK: image ${IMAGE_NAME}:${IMAGE_TAG} exists"
                        docker image inspect ${IMAGE_NAME}:${IMAGE_TAG} \
                          --format 'Id={{.Id}} Created={{.Created}} Size={{.Size}} bytes'
                        docker images ${IMAGE_NAME}
                    else
                        echo "VERIFY FAILED: image ${IMAGE_NAME}:${IMAGE_TAG} not found"
                        exit 1
                    fi
                '''
            }
        }
    }

    post {
        success {
            echo "SUCCESS: ${env.IMAGE_NAME}:${env.IMAGE_TAG} built and verified for ${env.STUDENT_NAME}"
        }
        failure {
            echo "FAILURE: pipeline of ${env.STUDENT_NAME} stopped. Open the failed stage log above to see the cause."
        }
        always {
            echo "Pipeline finished with status: ${currentBuild.currentResult}"
        }
    }
}
