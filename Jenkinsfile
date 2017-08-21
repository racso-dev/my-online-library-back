import com.steamulo.CommonHelper
properties ([gitLabConnection('jenkins'), buildDiscarder(logRotator(artifactDaysToKeepStr: '5', artifactNumToKeepStr: '10', daysToKeepStr: '10', numToKeepStr: '5'))])
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
                        sh "printf \"Infos maven :\n Version : ${pom.version}\n\nInfos Git :\n Commit : ${gitCommit}\n Url : ${gitUrl}\n Branch : ${env.BRANCH_NAME}\n\nInfos sur le build :\n Build number : $BUILD_NUMBER\n Build tag : $BUILD_TAG\" > project_version.txt"
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
                    if (!pom.version.contains("SNAPSHOT") && "master".equals("${env.BRANCH_NAME}")) {
                        sshagent (credentials: ['gitlab-key']) {
                            def gitTags = sh(script: 'git ls-remote --tags origin', returnStdout: true)
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
                    if (!pom.version.contains("SNAPSHOT")) {
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
                    withAWS (
                        credentials: "s3-delivery",
                        region: "eu-west-1"
                    ) {
                        dir('target') {
                            s3Upload(file: "market-pay-api-${pom.version}.jar", bucket: "delivery.steamulo.org", path: "market-pay/market-pay-api-${pom.version}.jar")
                        }
                    }
                }
            }
            stage ('Deploying') {
                gitlabCommitStatus('Deploying') {
                    if ("${env.BRANCH_NAME}" == "integration") {
                        git branch: 'stable', credentialsId: 'gitlab-key', url: 'git@git.steamulo.com:carrefour-banque/market-pay-ansible.git'
                        sshagent (credentials: ['gitlab-key']) {
                            sh "ansible-galaxy install -p ./roles -r ./roles/requirements.yml"
                        }
                        withCredentials([[$class: 'StringBinding', credentialsId: 'vault-marketpay', variable: 'TOKEN']]) {
                            sh 'echo $TOKEN > .market_pay_vault_pass.txt'
                        }
                        try {
                            sshagent(['ssh-key']) {
                                sh "ANSIBLE_HOST_KEY_CHECKING=False ansible-playbook -l mpay-int-front -i hosts/hosts-int --skip-tags postfix,logrotate,nginx -t configuration,deploy -u market-pay-api -e \"{'ansible_become': false, 'forceDownloadBuild': true, 'version_to_deploy_api':'${pom.version}'}\" mpay_install.yml"
                            }
                            slackSend channel: '#marketpay', color: 'good', message: 'API déployée avec succès : http://marketpay-int.steamulo.org/', teamDomain: 'steamulo', token: 'yanddPUfDw5vvIAu9PYaviom'
                        } catch (e) {
                            slackSend channel: '#marketpay', color: 'danger', message: "Erreur lors du déploiment de l'API voir : http://ci.steamulo.com/blue/organizations/jenkins/Market%20Pay%2FAPI/detail/${env.BRANCH_NAME}/${BUILD_NUMBER}/pipeline", teamDomain: 'steamulo', token: 'yanddPUfDw5vvIAu9PYaviom'
                        }
                    }
                }
            }
        }
    } catch (e) {
        slackSend channel: '#marketpay', color: 'danger', message: "Erreur lors du build de l'API, voir : http://ci.steamulo.com/blue/organizations/jenkins/Market%20Pay%2FAPI/detail/${env.BRANCH_NAME}/${BUILD_NUMBER}/pipeline", teamDomain: 'steamulo', token: 'yanddPUfDw5vvIAu9PYaviom'
        throw e
    } finally {
        cleanWs notFailBuild: true
    }
}
