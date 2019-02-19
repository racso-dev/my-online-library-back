properties([gitLabConnection('jenkins'), buildDiscarder(logRotator(artifactDaysToKeepStr: '5', artifactNumToKeepStr: '10', daysToKeepStr: '10', numToKeepStr: '5'))])
import com.steamulo.CommonHelper

node ('web') {
    try {
        stage 'Checkout'
        checkout scm
        def pom = readMavenPom file: 'pom.xml'
        def gitCommit = sh(script: 'git rev-parse --verify HEAD', returnStdout: true).trim()
        def gitUrl = sh(script: 'git config --get remote.origin.url', returnStdout: true).trim()
        def isPrimaryBranch = (!pom.version.contains("SNAPSHOT")
                                                    && ("${env.BRANCH_NAME}" == "master"
                                                    || "${env.BRANCH_NAME}" == "recette"))
        gitlabBuilds(builds: ['Build JAR', 'Add git tag', 'SonarQube analysis']) {
            stage 'Build JAR'
            gitlabCommitStatus('Build JAR') {
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
            stage 'Add git tag'
            gitlabCommitStatus('Add git tag') {
                if (!pom.version.contains("SNAPSHOT") && "${env.BRANCH_NAME}" == "recette") {
                    sshagent (credentials: ['gitlab-key']) {
                        def gitTags = sh(script: 'git ls-remote --tags origin', returnStdout: true)
                        sh 'git config --global user.email "jenkins@steamulo.com"'
                        sh 'git config --global user.name "Jenkins Steamulo"'
                        sh "git tag -a ${pom.version} -m \"Jenkins build #$BUILD_NUMBER\""
                        sh "git push --tags"
                    }
                }
            }
            stage 'SonarQube analysis'
            gitlabCommitStatus('SonarQube analysis') {
               if (isPrimaryBranch) {
                    withMaven(
                        maven: 'mvn 3.3.3',
                        mavenSettingsConfig: 'steamulo-maven-settings',
                        mavenLocalRepo: '.repository'
                    ){
                        sh "mvn compile"
                    }
                    def scannerHome = tool 'SonarQube Scanner 3.0';
                    withSonarQubeEnv('Sonar-CI') {
                          sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectVersion=${pom.version}"
                    }
                }
            }
            if (isPrimaryBranch) {
                stage 'Uploading build to S3'
                gitlabCommitStatus('Uploading build to S3') {
                    withAWS (
                        credentials: "s3-delivery",
                        region: "eu-west-1"
                    ) {
                        dir('target') {
                            s3Upload(file: "api-starter-${pom.version}.jar", bucket: "delivery.steamulo.org", path: "steamulo/api-starter-${pom.version}-${env.BRANCH_NAME}.jar")
                        }
                    }
                }
                if ("${env.BRANCH_NAME}" == "recette") {
                    stage 'Deploying'
                    gitlabCommitStatus('Deploying') {
                        git branch: 'stable', credentialsId: 'gitlab-key', url: 'git@git.steamulo.com:steamulo/steamulo-ansible-starter.git'
                        sshagent (credentials: ['gitlab-key']) {
                            sh "ansible-galaxy install -p ./roles -r ./roles/requirements.yml"
                        }
                        withCredentials([[$class: 'StringBinding', credentialsId: 'vault-api-starter', variable: 'TOKEN']]) {
                            sh 'echo $TOKEN > .starter_vault_pass.txt'
                        }
                        if ("${env.BRANCH_NAME}" == "recette") {
                            try {
                                sshagent(['ssh-key']) {
                                    sh "ANSIBLE_HOST_KEY_CHECKING=False ansible-playbook --skip-tags mysql,nginx,postfix,logrotate -t configuration,deploy -u gestion-tpe-api -i hosts/hosts-recette -e \"{'ansible_become': false, 'forceDownloadBuild': true, 'version_to_deploy_api' : '${pom.version}-${env.BRANCH_NAME}'}\" gestion_tpe_install.yml"
                                }
                                // À DÉCOMMENTER ET ÉDITER POUR AJOUTER UNE NOTIFICATION SLACK DE SUCCÈS
                                //slackSend channel: '#mon-channel', color: 'good', message: "API déployée avec succès en recette : https://starter-recette.steamulo.org", teamDomain: 'steamulo', token: 'yanddPUfDw5vvIAu9PYaviom'
                            } catch (e) {
                                // À DÉCOMMENTER ET ÉDITER POUR AJOUTER UNE NOTIFICATION SLACK D'ÉCHEC
                                //slackSend channel: '#mon-channel', color: 'danger', message: "Erreur lors du déploiment d'API en recette, voir : http://ci.steamulo.com/job/Steamulo/job/api-starter/job/${env.BRANCH_NAME}/", teamDomain: 'steamulo', token: 'yanddPUfDw5vvIAu9PYaviom'
                                throw e
                            }
                        }
                    }
                }
            }
        }
    } catch (e) {
        // À DÉCOMMENTER ET ÉDITER POUR AJOUTER UNE NOTIFICATION SLACK D'ÉCHEC
        //slackSend channel: '#mon-channel', color: 'danger', message: "Erreur lors du build de l'API api-starter voir : http://ci.steamulo.com/job/Steamulo/job/api-starter/job/${env.BRANCH_NAME}/", teamDomain: 'steamulo', token: 'yanddPUfDw5vvIAu9PYaviom'
        throw e
    } finally {
        cleanWs notFailBuild: true
    }
}
