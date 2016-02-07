package com.github.loyada.dollarx

import java.util.concurrent.TimeUnit

import org.openqa.selenium.WebDriver
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.startsWith
import org.junit.Assert.assertThat


import com.github.loyada.dollarx.ElementProperties._
import com.github.loyada.dollarx.Path._
import com.github.loyada.dollarx.custommatchers.hamcrest.CustomMatchers._

object ExampleJunitStyleTest {
  var inBrowser: Browser = _
  var driver : WebDriver = _

  @BeforeClass def setup() = {
    val driverPath = System.getenv.get("CHROMEDRIVERPATH")
    driver = DriverSetup(true).createNewDriver("chrome", driverPath)
    driver.get("http://www.google.com")
    driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS)
    inBrowser = new StandardBrowser(driver)
  }

  @AfterClass def teardown() {
    driver.quit()
  }
}

class ExampleJunitStyleTest {

  import ExampleJunitStyleTest._

  @Before def goToGoogle() {
    driver.get("http://www.google.com")
  }

  private def google = {
    val searchFormWrapper = element that(has id "searchform" ) containing form
    input inside searchFormWrapper
  }

  @Test
  def googleForAmazonAndVerifyFirstResult() {
    inBrowser sendKeys "amazon" to google
    val results = div that(has id "search")
    val resultsLink = anchor inside results
    val amazonAsFirstResult = first occurrenceOf resultsLink and (has textContaining "amazon.com")
    assertThat(amazonAsFirstResult, isPresentIn(inBrowser))
  }

  @Test
  def showAUsefulExceptionForOperationError() {
    val searchFormWrapper = element that(has id "searchform" ) containing(form) describedBy "search form"
    val warcraft = input.inside(searchFormWrapper).withText("for the horde!")
    try {
      inBrowser sendKeys "amazon" to warcraft
    }
    catch {
      case e: OperationFailedException => {
        assertThat(e.getMessage, equalTo("could not send keys to input, inside (search form), and has the text \"for the horde!\""))
        assertThat(e.getCause.getMessage, startsWith("could not find input, inside (search form), and has the text \"for the horde!\""))
      }
    }
  }

  @Test
  def googleForAmazonAndFeelingLucky() {
    inBrowser sendKeys "amazon" to google
    val results = div that (has id "search")
    val resultsLink = anchor inside results
    val firstSuggestion = first occurrenceOf listItem inside form
    inBrowser.hover over firstSuggestion
    val feelingLucky = anchor inside firstSuggestion withTextContaining "feeling lucky"
    inBrowser.click at feelingLucky
    val amazonMainTitle = title that (has textContaining "amazon") describedBy "amazon main title"
    assertThat(amazonMainTitle, isPresentIn(inBrowser))
  }

  @Test
  def googleForAmazonAssertionError1() {
    inBrowser sendKeys "amazon" to google
    val results = div that(has id "search")
    val resultsLink = anchor inside results describedBy "search result"
    val amazonResult = resultsLink that(has textContaining "amazon.com")
    try {
      assertThat(amazonResult, isPresent(1000).timesIn(inBrowser))
    }
    catch {
      case e: AssertionError => {
        e.printStackTrace
      }
    }
  }

  @Test
  def googleForAmazonAssertionError2() {
    inBrowser sendKeys "amazon" to google
    val results = div that(has id "search")
    val resultsLink = anchor inside results describedBy "search result"
    val warcraftResult = resultsLink(0) that(has text "for the horde!")
    try {
      assertThat(warcraftResult, isPresentIn(inBrowser))
    }
    catch {
      case e: AssertionError => {
        e.printStackTrace()
        assertThat(e.getMessage, equalTo("\nExpected: browser page contains the first occurrence of (search result), that has the text \"for the horde!\"" +
          "\n     but: (the first occurrence of (search result), that has the text \"for the horde!\") is absent"))
      }
    }
  }
}

