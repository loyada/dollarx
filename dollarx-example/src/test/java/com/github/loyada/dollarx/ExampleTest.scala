package com.github.loyada.dollarx

import java.util.concurrent.TimeUnit

import  com.github.loyada.dollarx.ElementProperties._
import  com.github.loyada.dollarx.Path._
import  com.github.loyada.dollarx.singlebrowser._
import  com.github.loyada.dollarx.singlebrowser.scalatestmatchers.CustomMatchers._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec, _}
import org.scalatest.exceptions.TestFailedException

class ExampleTest extends FunSpec with BeforeAndAfter with BeforeAndAfterAll with MustMatchers with MockitoSugar {


  val driverPath = System.getenv.get("CHROMEDRIVERPATH")
  val driver = DriverSetup(true).createNewDriver("chrome", driverPath)
  driver.get("http://www.google.com")
  driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

  // We are using the single browser "flavor" for simplicity
  SingleBrowser.singleDriver = driver

  describe("Googling for amazon") {
    val searchFormWrapper = has id "searchform" and contains(form)
    val google = input inside searchFormWrapper
    sendKeys("amazon") to google
   // or google.sendKeys("amazon")

    it("amazon.com should appear as the first result link") {
      val results = div that (has id "search")
      val resultsLink = anchor inside results
      val amazonAsFirstResult = first occurrenceOf resultsLink and (has textContaining "amazon")
      amazonAsFirstResult must be(present)
    }

    it("shows a good useful exception in case of a failure to perform an operation") {
      val result = div that (has id "search") describedBy "search result"
      try {
        click on (result withClass "foobar")
      } catch {
        case e: OperationFailedException =>
          e.printStackTrace()
          e.getMessage must be("could not click on (search result, that has class \"foobar\")")
          e.getCause.getMessage must startWith("could not find (search result, that has class \"foobar\")")
      }
    }

    it("creates a clear assertion error #1") {
      val results = div that (has id "search")
      val resultsLink = anchor inside results describedBy "search result"
      val amazonResult = resultsLink that (has textContaining "amazon")
      try {
        amazonResult must appear(1000 timesOrMore)
      } catch {
        case e: TestFailedException =>
          e.printStackTrace()
          e.getMessage must fullyMatch regex """\(search result, that has text containing "amazon"\) should appear at least 1000 times, but it appears \d+ times"""
      }
    }

    it("creates a clear assertion error #2") {
      val results = div that (has id "search")
      val resultsLink = anchor inside results describedBy "search result link"
      val warcraftAsFirstResult = resultsLink(0) and (has text "for the horde!")
      try {
        warcraftAsFirstResult must not(be(absent))
      } catch {
        case e: TestFailedException =>
          e.printStackTrace()
          e.getMessage must be("(the first occurrence of (search result link), that has the text \"for the horde!\") is expected to be present, but is absent")
      }
    }

    it("using I'm feeling Lucky will direct to amazon.com") {
      val firstSuggestion = first occurrenceOf (listItem inside form)
      hover over firstSuggestion
      val feelingLucky = anchor inside firstSuggestion withTextContaining "feeling lucky"
      click on feelingLucky
      val amazonMainTitle = title that (has textContaining "amazon")
      amazonMainTitle must be(present)
    }
  }

  override def afterAll() {
    driver.close()
    driver.quit()
  }
}