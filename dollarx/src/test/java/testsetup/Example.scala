package testsetup

import org.scalatest.FunSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.mock.MockitoSugar
import info.dollarx.{InBrowser, Path}

import org.scalatest._
import InBrowser._
import Path._
import info.dollarx.ElementProperties._
import info.dollarx.scalatestmatchers.CustomMatchers._

class Example extends FunSpec with BeforeAndAfter with MustMatchers with MockitoSugar {


  before {
    InBrowser.driver = DriverSetup(true).createNewDriver("chrome", System.getProperty("CHROMEDRIVERPATH"))
    driver.get("www.google.com")
  }

  describe("Googling for amazon") {
    val searchFormWrapper = has id "searchform" and contains(form)
    val google = input inside searchFormWrapper
    sendKeys("amazon") to google

    it("amazon.com should appear as the first result") {
      val results = div that (has id "search")
      val resultsLinks = anchor inside results
      val amazonResult = resultsLinks(0) that (has textContaining ("amazon.com"))
      amazonResult must be(present)
    }

  }
}