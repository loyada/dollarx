node{
  stage ('Build') {
 
    git url: 'https://github.com/loyada/dollarx'
 
    withMaven(maven: 'Maven 3') {
      // Run the maven build
      sh "mvn clean install" 
    } 
  }
}
