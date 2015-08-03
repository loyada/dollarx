package info.dollarx

import ElementProperties.{hasText, hasClass}
import info.dollarx.singlebrowser.SingleBrowser
import info.dollarx.singlebrowser.scalatestmatchers.CustomMatchers
import org.openqa.selenium.{NoSuchElementException, By, WebDriver}
import org.scalatest.FunSpec
import org.scalatest.mock.MockitoSugar
import org.scalatest._
import Matchers._
import org.mockito.Mockito._

class InBrowserTest extends FunSpec with MustMatchers with MockitoSugar {
  import Path._
  import CustomMatchers._
  var driver = mock[WebDriver]

  SingleBrowser.singleDriver = driver

  describe("Browser") {

    it("should check for presence correctly") {
      val el = div withClass("foo") describedBy "Hulk"
      val xpath = el.getXPath.get
      when(driver.findElement(By.xpath("//" + xpath))).thenThrow(new NoSuchElementException("foo", new Exception()))
      el must be(present)
    }

    it("should check for absence correctly") {
      when(driver.findElement(By.xpath("/html[not(.//div)]"))).thenThrow(new NoSuchElementException("foo", new Exception()))
      span withClass "foo" should appear(5 times)
    }
  }
}