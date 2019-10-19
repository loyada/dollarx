node{
  stage ('Build') {
 
    git url: 'https://github.com/loyada/dollarx'
 
    withMaven(maven: 'maven 3') {
      // Run the maven build
      sh "mvn clean install" 
    } 
  }
}
