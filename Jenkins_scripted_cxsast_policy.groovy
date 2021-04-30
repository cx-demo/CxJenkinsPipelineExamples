properties([pipelineTriggers([githubPush()])])
 
pipeline {
    /*
    //specify nodes for executing
    agent {
        label 'github-ci'
    }
    */
    agent any
    
    stages {
        /* checkout repo */
        stage('Checkout SCM') {
            steps {
                checkout([
                 $class: 'GitSCM',
                 branches: [[name: 'jenkins-ci']],
                 userRemoteConfigs: [[
                    url: 'https://github.com/cx-demo/JavaVulnerableLab.git',
                    credentialsId: 'mygithub',
                 ]]
                ])
            }
        }
        stage('Checkmarx') {
            steps{
                step([$class: 'CxScanBuilder', addGlobalCommenToBuildCommet: true, comment: '', configAsCode: true, credentialsId: '', enableProjectPolicyEnforcement: true, excludeFolders: '', exclusionsSetting: 'global', failBuildOnNewResults: false, failBuildOnNewSeverity: 'HIGH', filterPattern: '''!**/_cvs/**/*, !**/.svn/**/*,   !**/.hg/**/*,   !**/.git/**/*,  !**/.bzr/**/*, !**/bin/**/*,
!**/obj/**/*,  !**/backup/**/*, !**/.idea/**/*, !**/*.DS_Store, !**/*.ipr,     !**/*.iws,
!**/*.bak,     !**/*.tmp,       !**/*.aac,      !**/*.aif,      !**/*.iff,     !**/*.m3u, !**/*.mid, !**/*.mp3,
!**/*.mpa,     !**/*.ra,        !**/*.wav,      !**/*.wma,      !**/*.3g2,     !**/*.3gp, !**/*.asf, !**/*.asx,
!**/*.avi,     !**/*.flv,       !**/*.mov,      !**/*.mp4,      !**/*.mpg,     !**/*.rm,  !**/*.swf, !**/*.vob,
!**/*.wmv,     !**/*.bmp,       !**/*.gif,      !**/*.jpg,      !**/*.png,     !**/*.psd, !**/*.tif, !**/*.swf,
!**/*.jar,     !**/*.zip,       !**/*.rar,      !**/*.exe,      !**/*.dll,     !**/*.pdb, !**/*.7z,  !**/*.gz,
!**/*.tar.gz,  !**/*.tar,       !**/*.gz,       !**/*.ahtm,     !**/*.ahtml,   !**/*.fhtml, !**/*.hdm,
!**/*.hdml,    !**/*.hsql,      !**/*.ht,       !**/*.hta,      !**/*.htc,     !**/*.htd, !**/*.war, !**/*.ear,
!**/*.htmls,   !**/*.ihtml,     !**/*.mht,      !**/*.mhtm,     !**/*.mhtml,   !**/*.ssi, !**/*.stm,
!**/*.stml,    !**/*.ttml,      !**/*.txn,      !**/*.xhtm,     !**/*.xhtml,   !**/*.class, !**/*.iml, !Checkmarx/Reports/*.*''', fullScanCycle: 10, groupId: '1', password: '{AQAAABAAAAAQRbUkKzVxzPQp3cddKK6yu07L5pMqzqHTjg/2vU4bvpM=}', preset: '36', projectName: 'JavaVulnerableLab_Pipeline', sastEnabled: true, serverUrl: 'http://192.168.137.50', sourceEncoding: '1', username: '', vulnerabilityThresholdResult: 'FAILURE', waitForResultsEnabled: true])
                echo "Caught: ${currentBuild.result}"
            }
            post {
                failure {
                    echo ">> Failure in Checkmarx"
                    error "Scan exceeded threshold or new vulnerabilities found"
                }
            }
        }
        stage('Do the deployment') {
            steps {
                echo ">> Run deploy applications "
            }
        }
    }
 
    /* Cleanup workspace */
    post {
       always {
           deleteDir()
       }
   }
}