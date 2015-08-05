package info.dollarx

import info.dollarx.ElementProperties._
import info.dollarx.Path._
import info.dollarx.singlebrowser.SingleBrowser
import info.dollarx.singlebrowser.scalatestmatchers.CustomMatchers._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec, _}

class ExampleTest extends FunSpec with BeforeAndAfter with MustMatchers with MockitoSugar {


     val driverPath = System.getenv.get("CHROMEDRIVERPATH")
     val driver = DriverSetup(true).createNewDriver("chrome", driverPath)
     driver.get("http://www.google.com")
   // We are using the single browser "flavor" for simplicity
     SingleBrowser.singleDriver = driver

   describe("Googling for amazon") {
     val searchFormWrapper = has id "searchform" and contains(form)
     val google = input inside searchFormWrapper
     sendKeys("amazon") to google

     it("amazon.com should appear as the first result link") {
       val results = div that (has id "search")
       val resultsLinks = anchor inside results
       val amazonResult = resultsLinks(0) that (has textContaining ("amazon.com"))
       amazonResult must be(present)
     }

   }

  after {
    driver.close()
    driver.quit()
  }
 }