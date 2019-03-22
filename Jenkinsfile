import com.steamulo.CommonHelper
properties([gitLabConnection('jenkins'), buildDiscarder(logRotator(artifactDaysToKeepStr: '5', artifactNumToKeepStr: '10', daysToKeepStr: '10', numToKeepStr: '5'))])

def shouldTag = { pom, env ->
    !pom.version.contains("SNAPSHOT") && "${env.BRANCH_NAME}" == "master"
}

def shouldDoSonarAnalysis = { pom, env ->
    "${env.BRANCH_NAME}" == "master"
}

def shouldUploadBuildAndDeploy = { env ->
    "${env.BRANCH_NAME}" == "integration" || "${env.BRANCH_NAME}" == "recette"
}

def slackChannel = '#todo'
def slackToken = 'todo'
def slackTeam = 'steamulo'

node ('web') {
    try {
        checkout scm
        def pom = readMavenPom file: 'pom.xml'
        def gitCommit = sh(script: 'git rev-parse --verify HEAD', returnStdout: true).trim()
        def gitUrl = sh(script: 'git config --get remote.origin.url', returnStdout: true).trim()

        gitlabBuilds(builds: ['Build Jar', 'Add git tag', 'SonarQube analysis', 'Uploading build to S3', 'Deploying']) {
            stage ('Build Jar') {
                gitlabCommitStatus('Build Jar') {
                    dir('src/main/resources') {
                        sh "printf \"Infos maven :\n Version : ${pom.version}\n\nInfos Git :\n Commit : ${gitCommit}\n Url : ${gitUrl}\n Branch : ${env.BRANCH_NAME}\n\nInfos sur le build :\n Build number : $BUILD_NUMBER\" > project_version.txt"
                    }
                    withMaven(
                        maven: 'mvn 3.3.3',
                        mavenSettingsConfig: 'steamulo-maven-settings',
                        mavenLocalRepo: '.repository'
                    ) {
                        sh "mvn clean install"
                    }
                }
            }
            stage ('Add git tag') {
                gitlabCommitStatus('Add git tag') {
                    if (shouldTag(pom, env)) {
                        sshagent (credentials: ['gitlab-key']) {
                            sh 'git config --global user.email "jenkins@steamulo.com"'
                            sh 'git config --global user.name "Jenkins Steamulo"'
                            sh "git tag -a ${pom.version} -m \"Jenkins build #$BUILD_NUMBER\""
                            sh "git push --tags"
                        }
                    }
                }
            }
            stage ('SonarQube analysis') {
                gitlabCommitStatus('SonarQube analysis') {
                    if (shouldDoSonarAnalysis(pom, env)) {
                        withMaven(
                            maven: 'mvn 3.3.3',
                            mavenSettingsConfig: 'steamulo-maven-settings',
                            mavenLocalRepo: '.repository'
                        ) {
                            withSonarQubeEnv('Sonar-CI') {
                              sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
                            }
                        }
                    }
                }
            }
            stage ('Uploading build to S3') {
                gitlabCommitStatus('Uploading build to S3') {
                    if (shouldUploadBuildAndDeploy(env)) {
                        withAWS (
                            credentials: "s3-delivery",
                            region: "eu-west-1"
                        ) {
                            dir('target') {
                                s3Upload(file: "pcv-website-${pom.version}.jar", bucket: "delivery.steamulo.org", path: "steamulo/api-starter-${pom.version}-${env.BRANCH_NAME}.jar")
                            }
                        }
                    }
                }
            }
            stage ('Deploying') {
                gitlabCommitStatus('Deploying') {
                    if (shouldUploadBuildAndDeploy(env)) {
                        if ("${env.BRANCH_NAME}" == "integration") {
                            url = "https://starter-int.steamulo.org"
                            envName = "intégration"
                            envCode = "int"
                        } else if ("${env.BRANCH_NAME}" == "recette") {
                            url = "https://starter-rec.steamulo.org"
                            envName = "recette"
                            envCode = "rec"
                        }

                        git branch: 'stable', credentialsId: 'gitlab-key', url: 'git@git.steamulo.com:steamulo/steamulo-ansible-starter.git'
                        sshagent (credentials: ['gitlab-key']) {
                            sh "ansible-galaxy install -p ./roles -r ./roles/requirements.yml"
                        }
                        withCredentials([[$class: 'StringBinding', credentialsId: 'vault-api-starter', variable: 'TOKEN']]) {
                            sh 'echo $TOKEN > .starter_vault_pass.txt'
                        }
                        try {
                            sshagent(['ssh-key']) {
                                sh "ANSIBLE_HOST_KEY_CHECKING=False ansible-playbook -l api -i hosts/hosts-${envCode} --skip-tags nginx,logrotate -t configuration,deploy -u starter -e \"{'ansible_become': false, 'forceDownloadBuild': true, 'version_to_deploy':'${pom.version}-${env.BRANCH_NAME}'}\" starter_install.yml"
                            }
                            slackSend channel: slackChannel, color: 'good', message: "${envName} déployée avec succès : ${url}", teamDomain: slackTeam, token: slackToken
                        } catch (e) {
                            slackSend channel: slackChannel, color: 'danger', message: "Erreur lors du déploiment de l'env ${envName} voir : http://ci.steamulo.com/blue/organizations/jenkins/<PROJECT>/detail/${env.BRANCH_NAME}/${BUILD_NUMBER}/pipeline", teamDomain: slackTeam, token: slackToken
                        }
                    }
                }
            }
        }
        if(currentBuild.previousBuild != null && currentBuild.previousBuild.result != 'SUCCESS'){
            slackSend channel: slackChannel, color: 'good', message: "Retour à la normal du build de l'API, voir : http://ci.steamulo.com/blue/organizations/jenkins/<PROJECT>/detail/${env.BRANCH_NAME}/${BUILD_NUMBER}/pipeline", teamDomain: slackTeam, token: slackToken
        }
    } catch (e) {
        slackSend channel: slackChannel, color: 'danger', message: "Erreur lors du build de l'API, voir : http://ci.steamulo.com/blue/organizations/jenkins/<PROJECT>/detail/${env.BRANCH_NAME}/${BUILD_NUMBER}/pipeline", teamDomain: slackTeam, token: slackToken
        throw e
    } finally {
        cleanWs notFailBuild: true
    }
}