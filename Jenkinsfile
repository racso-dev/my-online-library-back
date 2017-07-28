properties([gitLabConnection('jenkins')])
import com.steamulo.CommonHelper
node ('web') {
    try {
        stage 'Checkout'
        checkout scm
        def pom = readMavenPom file: 'pom.xml'
        def gitCommit = sh(script: 'git rev-parse --verify HEAD', returnStdout: true).trim()
        def gitUrl = sh(script: 'git config --get remote.origin.url', returnStdout: true).trim()
        gitlabBuilds(builds: ['Build ReactJS', 'Build Jar', 'Add git tag', 'SonarQube analysis', 'Artifacts', 'Uploading build to S3']) {
            stage 'Build ReactJS'
            gitlabCommitStatus('Build ReactJS') {
                dir('src/main/front') {
                    git branch: 'master', credentialsId: 'gitlab-key', url: 'git@git.steamulo.com:carrefour-banque/market-pay-webapp.git'
                    def helper = new CommonHelper()
                    withEnv(helper.buildEnvNvm('v6.10.0')) {
                        sh "npm install"
                        sh "npm run build"
                    }
                    sh "find . -mindepth 1 -name build -prune -o -exec rm -rf {} +"
                    sh "printf \"Infos maven :\n Version : ${pom.version}\n\nInfos Git :\n Commit : ${gitCommit}\n Url : ${gitUrl}\n Branch : ${env.BRANCH_NAME}\n\nInfos sur le build :\n Build number : $BUILD_NUMBER\n Build tag : $BUILD_TAG\" > build/project_version.txt"
                }
            }
            stage 'Build Jar'
            gitlabCommitStatus('Build Jar') {
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
                sshagent (credentials: ['gitlab-key']) {
                    def gitTags = sh(script: 'git ls-remote --tags origin', returnStdout: true)
                    sh 'git config --global user.email "jenkins@steamulo.com"'
                    sh 'git config --global user.name "Jenkins Steamulo"'
                    if (pom.version.contains("SNAPSHOT") && gitTags.contains(pom.version)) {
                        sh "git tag -d ${pom.version}"
                        sh "git push origin :refs/tags/${pom.version}"
                    }
                    sh "git tag -a ${pom.version} -m \"Jenkins build #$BUILD_NUMBER\""
                    sh "git push --tags"
                }
            }
            stage 'SonarQube analysis'
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
            stage 'Uploading build to S3'
            gitlabCommitStatus('Uploading build to S3') {
                withAWS (
                    credentials: "s3-delivery",
                    region: "eu-west-1"
                ) {
                    dir('target') {
                        s3Upload(file: "market-pay-api-${pom.version}.jar", bucket: "delivery.steamulo.org", path: "market-pay/market-pay-${pom.version}.jar")
                    }
                }
            }
        }
    } finally {
        cleanWs notFailBuild: true
    }
}
