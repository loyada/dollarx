package info.dollarx.singlebrowser


import java.util

import info.dollarx.Path
import info.dollarx.singlebrowser.scalatestmatchers.CustomMatchers
import org.mockito.Mockito._
import org.openqa.selenium.{WebElement, By, NoSuchElementException, WebDriver}
import org.scalatest.Matchers._
import org.scalatest.{FunSpec, _}
import org.scalatest.exceptions.TestFailedException
import org.scalatest.mock.MockitoSugar

class CustomMatchersTest extends FunSpec with MustMatchers with MockitoSugar {
  import CustomMatchers._
  import Path._
  var driver = mock[WebDriver]

  SingleBrowser.singleDriver = driver

  describe("Browser") {

    it(" check for presence correctly") {
      val el = div withClass("foo") describedBy "Hulk"
      val xpath = el.getXPath.get
      when(driver.findElement(By.xpath("//" + xpath))).thenThrow(new NoSuchElementException("foo", new Exception()))
      try{
        el must be(present)
        fail("should fail")
      } catch {
        case e: TestFailedException => e.getMessage() must equal("Hulk is expected to be present, but is absent")
      }
    }

    it(" check for apear n times correctly") {
      reset(driver)
      when(driver.findElement(By.xpath("/html[count(//span)=5]"))).thenThrow(new NoSuchElementException("foo", new Exception()))
      try{
        span should appear(5 times)
        fail("should fail")
      } catch {
        case e: TestFailedException => e.getMessage() must equal("span should appear 5 times, but it appears 0 times")
      }
    }
    it("should check for at least n times correctly") {
      reset(driver)
      when(driver.findElement(By.xpath("/html[count(//span)>=5]"))).thenThrow(new NoSuchElementException("foo", new Exception()))
      when(driver.findElements(By.xpath("//" + span.getXPath.get))).thenReturn(util.Arrays.asList(mock[WebElement]))
      try{
        span should appear(5 timesOrMore)
        fail("should fail")
      } catch {
        case e: TestFailedException => e.getMessage() must equal("span should appear at least 5 times, but it appears 1 times")
      }
    }
  }
}