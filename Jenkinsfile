import com.steamulo.CommonHelper
properties([disableConcurrentBuilds(), buildDiscarder(logRotator(artifactDaysToKeepStr: '5', artifactNumToKeepStr: '10', daysToKeepStr: '10', numToKeepStr: '5'))])

def shouldTag = { pom, env ->
    !pom.version.contains("SNAPSHOT") && ("${env.BRANCH_NAME}" == "master" || "${env.BRANCH_NAME}" == "hotfix")
}

def shouldDoSonarAnalysis = { pom, env ->
    "${env.BRANCH_NAME}" == "master"
}

def shouldDeploy = { env ->
    "${env.BRANCH_NAME}" == "integration" || "${env.BRANCH_NAME}" == "recette"
}

def mattermostChannel = 'todo'

def checksum = 'xxx'

node ('web') {
    try {
        checkout scm
        def pom = readMavenPom file: 'pom.xml'
        def gitCommit = sh(script: 'git rev-parse --verify HEAD', returnStdout: true).trim()
        def gitUrl = sh(script: 'git config --get remote.origin.url', returnStdout: true).trim()

        stage ('Build Jar') {
            dir('src/main/resources') {
                sh "printf \"Infos maven :\n Version : ${pom.version}\n\nInfos Git :\n Commit : ${gitCommit}\n Url : ${gitUrl}\n Branch : ${env.BRANCH_NAME}\n\nInfos sur le build :\n Build number : $BUILD_NUMBER\" > project_version.txt"
            }
            withEnv(["JAVA_HOME=${ tool 'Java 11' }", "PATH+MAVEN=${tool 'mvn 3.6.2'}/bin:${env.JAVA_HOME}/bin"]) {
                sh "mvn clean install"
            }
            dir('target') {
                checksum = sh(script: 'sha1sum api-starter-' + pom.version + '.jar | cut -d " " -f 1', returnStdout: true).trim()
                echo "sha1sum: ${checksum}"
            }
        }
        stage ('Add git tag') {
            if (shouldTag(pom, env)) {
                sshagent (credentials: ['gitlab-key']) {
                    sh 'git config --global user.email "jenkins@steamulo.com"'
                    sh 'git config --global user.name "Jenkins Steamulo"'
                    sh "git tag -a ${pom.version} -m \"Jenkins build #$BUILD_NUMBER sha1-cheksum : ${checksum}\""
                    sh "git push --tags"
                    withEnv(["JAVA_HOME=${ tool 'Java 11' }", "PATH+MAVEN=${tool 'mvn 3.6.2'}/bin:${env.JAVA_HOME}/bin"]) {
                        sh "mvn org.codehaus.mojo:versions-maven-plugin:2.7:set -DnextSnapshot=true"
                    }
                    newPom = readMavenPom file: 'pom.xml'
                    sh "git add pom.xml"
                    sh "git commit -m \"[RELEASE] bump version to ${newPom.version}\""
                    sh "git push origin HEAD:${env.BRANCH_NAME}"
                }
            }
        }
        stage ('SonarQube analysis') {
            if (shouldDoSonarAnalysis(pom, env)) {
                def scannerHome = tool 'SonarQube Scanner 4.4';
                withEnv(["JAVA_HOME=${ tool 'Java 11' }", "PATH+MAVEN=${tool 'mvn 3.6.2'}/bin:${env.JAVA_HOME}/bin"]) {
                    withSonarQubeEnv('Sonar-CI') {
                        sh "${scannerHome}/bin/sonar-scanner"
                    }
                }
            }
        }
        stage ('Uploading build to S3') {
            if (shouldDeploy(env) || shouldTag(pom, env)) {
                withAWS (
                    credentials: "s3-delivery",
                    region: "eu-west-1"
                ) {
                    dir('target') {
                        s3Upload(file: "steamulo-api-starter-${pom.version}.jar", bucket: "delivery.steamulo.org", path: "steamulo/java-starter/java-starter-starter-${pom.version}-${env.BRANCH_NAME}.jar", metadatas:["sha1-checksum:${checksum}"])
                    }
                }
            }
        }
        stage ('Deploying') {
            if (shouldDeploy(env)) {
                if ("${env.BRANCH_NAME}" == "integration") {
                    url = "https://starter-int.steamulo.org"
                    envName = "intégration"
                    envCode = "int"
                } else if ("${env.BRANCH_NAME}" == "recette") {
                    url = "https://starter-rec.steamulo.org"
                    envName = "recette"
                    envCode = "rec"
                }

                withPythonEnv('/usr/bin/python3') {
                    git branch: 'stable', credentialsId: 'gitlab-key', url: 'git@git.steamulo.com:steamulo/steamulo-ansible-starter.git'
                    sh "python -m pip install --upgrade pip"
                    sh "python -m pip install -r requirements.txt"
                    sshagent (credentials: ['gitlab-key']) {
                        sh "ansible-galaxy install -p ./roles -r ./roles/requirements.yml"
                    }
                    withCredentials([[$class: 'StringBinding', credentialsId: 'vault-api-starter', variable: 'TOKEN']]) {
                        sh 'echo $TOKEN > .starter_vault_pass.txt'
                    }
                    try {
                        sshagent(['ssh-key']) {
                            sh "ANSIBLE_HOST_KEY_CHECKING=False ansible-playbook -i hosts/hosts_${envCode} -t steamengine_deploy -u starter -e \"{'ansible_become': false, 'steamengine_build_url':'https://delivery.steamulo.org/steamulo/java-starter/java-starter-${pom.version}-${env.BRANCH_NAME}', 'steamengine_build_checksum':'sha1:${checksum}'}\" starter_install.yml"
                        }
                        mattermostSend channel: mattermostChannel, color: 'good', message: "${envName} déployée avec succès : ${url}"
                    } catch (e) {
                        mattermostSend channel: mattermostChannel, color: 'danger', message: "Erreur lors du déploiment de l'env ${envName} voir : https://ci.steamulo.com/<PROJECT JOB PATH>/job/${env.BRANCH_NAME}/${BUILD_NUMBER}/consoleFull"
                        throw e
                    }
                }
            }
        }
        if(currentBuild.previousBuild != null && currentBuild.previousBuild.result != 'SUCCESS'){
            mattermostSend channel: mattermostChannel, color: 'good', message: "Retour à la normal du build de l'API, voir : https://ci.steamulo.com/<PROJECT JOB PATH>/job/${env.BRANCH_NAME}/${BUILD_NUMBER}/consoleFull"
        }
    } catch (e) {
        mattermostSend channel: mattermostChannel, color: 'danger', message: "Erreur lors du build de l'API, voir : https://ci.steamulo.com/<PROJECT JOB PATH>/job/${env.BRANCH_NAME}/${BUILD_NUMBER}/consoleFull"
        throw e
    } finally {
        cleanWs()
    }
}
