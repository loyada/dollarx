node{
    stage ('Build without Docker') {
 
    git url: 'https://github.com/loyada/dollarx'
 
    withMaven(maven: 'maven-3') {
      // Run the maven build
      sh "mvn clean install" 
     } 
    }  

// Build with docker. 
// Everything below this point is relevant only if you want to build using a container, instead of 
// relying on the pipeline-maven-plugin of Jenkins. It stands by itself - it does not require anything 
// in the "Build without Docker" stage.
    
    
    def myMavenContainer = docker.image("maven:3.6.2-jdk-12")
    myMavenContainer.pull()
    stage('prep') {
        checkout scm
    }
    
    stage ('Build With Docker') {
// The volume mapping is useful for caching of libraries to speed up builds. It is optional. Ensure to create the 
// directory /var/jenkins_home/.m2/repository , and do "chown 1000:1000 repository" so that builds can write to it.
      myMavenContainer.inside("-v ${env.home}:/root/.m2") {
        sh "mvn clean install" 
      }
  }
}
