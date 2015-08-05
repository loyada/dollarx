package info.dollarx

import info.dollarx.ElementProperties._
import info.dollarx.Path._
import info.dollarx.singlebrowser.SingleBrowser
import info.dollarx.singlebrowser.scalatestmatchers.CustomMatchers._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec, _}
import org.scalatest.exceptions.TestFailedException

class ExampleTest extends FunSpec with BeforeAndAfter with BeforeAndAfterAll with MustMatchers with MockitoSugar {


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
       val resultsLink = anchor inside results
       val amazonResult = first occuranceOf resultsLink that (has textContaining "amazon.com")
       amazonResult must be(present)
     }



     it("creates a clear assertion error #1") {
       val results = div that (has id "search")
       val resultsLink = anchor inside results describedBy  "search result"
       val amazonResult = resultsLink that (has textContaining "amazon.com")
       try{
         amazonResult must appear(1000 times)
       } catch {
         case e: TestFailedException => println(e)
       }
     }

     it("creates a clear assertion error #2") {
       val results = div that (has id "search")
       val resultsLink = anchor inside results describedBy("search result link")
       val amazonResult = resultsLink(0) that (has textContaining "xyzzzz.com")
       try{
         amazonResult must be(present)
       } catch {
         case e: TestFailedException => println(e)
       }
     }

     it("using I'm feeling Lucky will direct to amazon.com") {
       val firstSuggestion = first occuranceOf(listItem inside form)
       hover over firstSuggestion
       val feelingLucky = anchor inside firstSuggestion withTextContaining "feeling lucky"
       click on feelingLucky
       val amazonMainTitle = title that (has textContaining "amazon") describedBy "amazon main title"
       amazonMainTitle must be(present)
     }

   }

  override def afterAll()  {
    driver.close()
    driver.quit()
  }
 }