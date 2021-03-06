package com.github.loyada.dollarx

import com.github.loyada.dollarx.Path._
import org.scalatest.exceptions.TestFailedException
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSpec, MustMatchers}
import com.github.loyada.dollarx.custommatchers.scalatest.CustomMatchers._
import com.github.loyada.dollarx.ElementProperties._


class ExampleMultiBrowserTest extends FunSpec with BeforeAndAfter with BeforeAndAfterAll with MustMatchers with MockitoSugar {


  val driverPath = System.getenv.get("CHROMEDRIVERPATH")
  val myDriver = DriverSetup(true).createNewDriver("chrome", driverPath)
  myDriver.get("http://www.google.com")
  val browser = new StandardBrowser(myDriver)

  describe("Googling for amazon") {
    val searchFormWrapper = has id "searchform" and contains(form)
    val google = input inside searchFormWrapper
    browser sendKeys "amazon" to google

    it("amazon.com should appear as the first result link") {
      val results = div that (has id "search")
      val resultsLink = anchor inside results
      val amazonResult = first occurrenceOf resultsLink that (has textContaining "amazon")
      amazonResult must be(present in browser)
    }

    it("shows a good useful exception in case of a failure to perform an operation") {
      val results = div that (has id "search") describedBy "search results"
      try {
        browser.click on (results withClass "foobar")
      } catch {
        case e: OperationFailedException =>
          e.printStackTrace()
          e.getMessage must be("could not click on (search results, that has class \"foobar\")")
          e.getCause.getMessage must startWith ("could not find (search results, that has class \"foobar\")")
      }
    }

    it("creates a clear assertion error #1") {
      val results = div that (has id "search")
      val resultsLink = anchor inside results describedBy  "search result"
      val amazonResult = resultsLink that (has textContaining "amazon")
      try{
        amazonResult must (appear(1000 times) in browser)
      } catch {
        case e: TestFailedException =>
          e.printStackTrace()
          e.getMessage must fullyMatch regex """\(search result, that has text containing "amazon"\) should appear 1000 times, but it appears \d+ times"""
      }
    }

    it("creates a clear assertion error #2") {
      val results = div that (has id "search")
      val resultsLink = anchor inside results describedBy "search result link"
      val warcraftResult = resultsLink(0) that (has text "for the horde!")
      try{
        warcraftResult must be(present in browser)
      } catch {
        case e: TestFailedException =>
          e.printStackTrace()
          e.getMessage must be ("(the first occurrence of (search result link), that has the text \"for the horde!\") is expected to be present, but is absent")
      }
    }

    it("using I'm feeling Lucky will direct to amazon.com") {
      val firstSuggestion = first occurrenceOf(listItem inside form)
      browser.hover over firstSuggestion
      val feelingLucky = anchor inside firstSuggestion withTextContaining "feeling lucky"
      browser.click on feelingLucky
      val amazonMainTitle = title that (has textContaining "amazon") describedBy "amazon main title"
      amazonMainTitle must be(present in browser)
    }
  }

  override def afterAll()  {
    myDriver.close()
    myDriver.quit()
  }
}