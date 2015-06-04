package dollarx

import com.gargoylesoftware.htmlunit.ElementNotFoundException
import dollarx.ElementProperties.{hasText, hasClass}
import org.openqa.selenium.{NoSuchElementException, By, WebDriver}
import org.scalatest.FunSpec
import org.scalatest.mock.MockitoSugar
import scalaTestMatchers.CustomMatchers
import CustomMatchers._
import org.scalatest._
import Matchers._
import org.mockito.Matchers._
import org.mockito.Mockito._

class InBrowserTest extends FunSpec with MustMatchers with MockitoSugar  {
  import ElementProperties._
  import WebEl._
  import InBrowser._
  InBrowser.driver = mock[WebDriver]



  describe("Browser") {

    it("should check for presence correctly") {
      val el = div withClass("foo")
      val xpath = el.getXPath().get
      when(InBrowser.driver.findElement(By.xpath("//" + xpath))).thenThrow(new NoSuchElementException("foo", new Exception()))
      el must be(present)
    }
    it("should check for absence correctly") {
      when(InBrowser.driver.findElement(By.xpath("/html[not(.//div)]"))).thenThrow(new NoSuchElementException("foo", new Exception()))
      div must be(absent)
      findAll(div) should have size 5

      div should appear(5 times)
    }
  }
}