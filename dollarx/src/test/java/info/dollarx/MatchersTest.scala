package info.dollarx

import java.util

import info.dollarx.custommatchers.scalatest.CustomMatchers
import org.mockito.Mockito._
import org.openqa.selenium.{WebElement, WebDriver, NoSuchElementException, By}
import org.scalatest.mock.MockitoSugar
import org.scalatest.{MustMatchers, FunSpec}


class MatchersTest extends FunSpec with MustMatchers with MockitoSugar {
  import Path._
  import CustomMatchers._

  var driverMock = mock[WebDriver]
  val browser = new Browser {
    override protected var driver: WebDriver = driverMock
  }

  describe("Browser") {

    it("should check for presence correctly") {
      val el = div withClass("foo") describedBy "Hulk"
      val xpath = el.getXPath.get
      when(driverMock.findElement(By.xpath("//" + xpath))).thenThrow(new NoSuchElementException("foo", new Exception()))
      el must be(custommatchers.scalatest.CustomMatchers.present in browser)
    }

    it("should check for absence correctly") {
      when(driverMock.findElement(By.xpath("/html[count(//span[contains(concat(' ', @class, ' '), ' foo ')])=5]"))).thenThrow(new NoSuchElementException("foo", new Exception()))
      when(driverMock.findElements(By.xpath("//span[contains(concat(' ', @class, ' '), ' foo ')]"))).thenReturn(java.util.Arrays.asList(mock[WebElement], mock[WebElement]))
      span withClass "foo" must (appear(5 times) in browser)
    }
  }

}
