node{
    stage ('Build without Docker') {
 
    git url: 'https://github.com/loyada/dollarx'
 
    withMaven(maven: 'maven-3') {
      // Run the maven build
      sh "mvn clean install" 
     } 
    }  

# Build with docker
    
    def myMavenContainer = docker.image("maven:3.6.2-jdk-12")
    myMavenContainer.pull()
    stage('prep') {
        checkout scm
    }
    
    stage ('Build With Docker') {
      myMavenContainer.inside("-v ${env.home}:/root/.m2") {
        sh "mvn clean install" 
      }
  }
}
