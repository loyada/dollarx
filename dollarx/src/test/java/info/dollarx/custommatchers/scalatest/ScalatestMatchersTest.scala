package info.dollarx.custommatchers.scalatest


import info.dollarx.{custommatchers, Browser, Path, ElementProperties}
import ElementProperties.{hasText, hasClass}
import org.openqa.selenium.{WebElement, NoSuchElementException, By, WebDriver}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FunSpec, MustMatchers}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.exceptions.TestFailedException

class ScalatestMatchersTest extends FunSpec with MustMatchers with MockitoSugar with BeforeAndAfterEach with BeforeAndAfter{
  import Path._
  import CustomMatchers._

  var driverMock: WebDriver = _
  var browser: Browser = _


  describe("Browser") {
    driverMock = mock[WebDriver]
    browser = new Browser {
      override protected var driver: WebDriver = driverMock
    }

    describe("when element is not present") {
      val el = div withClass "foo" describedBy "Hulk"
      val xpath = el.getXPath.get
      it("shows correct error for present") {
        when(driverMock.findElement(By.xpath("//" + xpath))).thenThrow(new NoSuchElementException("foo", new Exception()))
        try {
          el must be(present in browser)
          fail("should fail")
        } catch {
          case e: TestFailedException => e.getMessage() must equal("Hulk is expected to be present, but is absent")
        }
      }
      it("shows correct error for haveElement") {
        reset(driverMock)
        when(driverMock.findElement(By.xpath("//" + xpath))).thenThrow(new NoSuchElementException("foo", new Exception()))

        try {
          browser must haveElement(el)
          fail("should fail")
        } catch {
          case e: TestFailedException => e.getMessage() must equal("Hulk is expected to be present, but is absent")
        }
      }
    }

    describe("when element is present") {
      reset(driverMock)
      val el = span withClass "foo" describedBy "Hulk"
      val xpath = el.getXPath.get
      when(driverMock.findElement(By.xpath("//" + xpath))).thenReturn(mock[WebElement])
      it("haveElement is successful") {
        el must be(present in browser)
        browser must haveElement(el)
      }
    }

    it(" shows error for presence n times correctly") {
      when(driverMock.findElement(By.xpath("/html[count(//span[contains(concat(' ', @class, ' '), ' foo ')])=5]"))).thenThrow(new NoSuchElementException("foo", new Exception()))
      when(driverMock.findElements(By.xpath("//span[contains(concat(' ', @class, ' '), ' foo ')]"))).thenReturn(java.util.Arrays.asList(mock[WebElement], mock[WebElement]))
      try{
      span withClass "foo" must (appear(5 times) in browser)
      fail("should fail")
      } catch {
        case e: TestFailedException => e.getMessage() must equal("span, that has class \"foo\" should appear 5 times, but it appears 2 times")
      }
    }
  }

}
