package com.github.loyada.dollarx.custommatchers.scalatest


import com.github.loyada.dollarx.{XpathUtils, Path}
import com.github.loyada.dollarx._
import ElementProperties.{hasText, hasClass}
import XpathUtils._
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
      override protected def driver: WebDriver = driverMock
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
      it("shows correct error for hasElement") {
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

    describe("when element is not absent") {
      val el = div withClass "foo" describedBy "Hulk"
      val xpath = DoesNotExistInEntirePage(el.getXPath.get)
      it("shows correct error for absent") {
        when(driverMock.findElement(By.xpath(xpath))).thenThrow(new NoSuchElementException("foo", new Exception()))
        try {
          el must be(absent in browser)
          fail("should fail")
        } catch {
          case e: TestFailedException => e.getMessage() must equal("Hulk is expected to be absent, but is present")
        }
      }
      it("shows correct error for hasNoElement") {
        try {
          browser must haveNoElement(el)
          fail("should fail")
        } catch {
          case e: TestFailedException => e.getMessage() must equal("Hulk is expected to be absent, but is present")
        }
      }
    }

    describe("when element is  absent") {
      reset(driverMock)
      val el = div withClass "bar"
      val xpath = DoesNotExistInEntirePage(el.getXPath.get)
      it("absent is successful") {
        when(driverMock.findElement(By.xpath(xpath))).thenReturn(mock[WebElement])
        el must be(absent in browser)
      }
      it("hasNoElement is successful") {
        when(driverMock.findElement(By.xpath(xpath))).thenReturn(mock[WebElement])
        browser must haveNoElement(el)

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

    describe("for element is apear n times") {
      reset(driverMock)
      it("functions for success") {
        when(driverMock.findElement(By.xpath("/html[count(//span[contains(concat(' ', @class, ' '), ' foo ')])=4]"))).thenReturn(mock[WebElement])
          span withClass "foo" must (appear(4 times) in browser)
      }

      it(" shows error for presence n times correctly") {
        when(driverMock.findElement(By.xpath("/html[count(//span[contains(concat(' ', normalize-space(@class), ' '), ' foo ')])=5]"))).thenThrow(new NoSuchElementException("foo", new Exception()))
        when(driverMock.findElements(By.xpath("//span[contains(concat(' ', normalize-space(@class), ' '), ' foo ')]"))).thenReturn(java.util.Arrays.asList(mock[WebElement], mock[WebElement]))
        try {
          span withClass "foo" must (appear(5 times) in browser)
          fail("should fail")
        } catch {
          case e: TestFailedException => e.getMessage() must equal("span, that has class \"foo\" should appear 5 times, but it appears 2 times")
        }
      }
    }

    describe("for element is apear at least n times") {
      reset(driverMock)
      it("functions for success") {
        when(driverMock.findElement(By.xpath("/html[count(//span[contains(concat(' ', @class, ' '), ' foo ')])>=3]"))).thenReturn(mock[WebElement])
        span withClass "foo" must (appear(3 timesOrMore ) in browser)
      }

      it(" shows error for presence n times correctly") {
        when(driverMock.findElement(By.xpath("/html[count(//span[contains(concat(' ', normalize-space(@class), ' '), ' foo ')])>=5]"))).thenThrow(new NoSuchElementException("foo", new Exception()))
        when(driverMock.findElements(By.xpath("//span[contains(concat(' ', normalize-space(@class), ' '), ' foo ')]"))).thenReturn(java.util.Arrays.asList(mock[WebElement], mock[WebElement]))
        try {
          span withClass "foo" must (appear(5 timesOrMore) in browser)
          fail("should fail")
        } catch {
          case e: TestFailedException => e.getMessage() must equal("span, that has class \"foo\" should appear at least 5 times, but it appears 2 times")
        }
      }
    }
    describe("for element is apear at most n times") {
      reset(driverMock)
      it("functions for success") {
        when(driverMock.findElement(By.xpath("/html[count(//span[contains(concat(' ', @class, ' '), ' foo ')])<=3]"))).thenReturn(mock[WebElement])
        span withClass "foo" must (appear(3 timesOrLess ) in browser)
      }

      it(" shows error for presence n times correctly") {
        when(driverMock.findElement(By.xpath("/html[count(//span[contains(concat(' ', normalize-space(@class), ' '), ' foo ')])<=3]"))).thenThrow(new NoSuchElementException("foo", new Exception()))
        when(driverMock.findElements(By.xpath("//span[contains(concat(' ', normalize-space(@class), ' '), ' foo ')]"))).thenReturn(java.util.Arrays.asList(mock[WebElement], mock[WebElement], mock[WebElement], mock[WebElement]))
        try {
          span withClass "foo" must (appear(3 timesOrLess) in browser)
          fail("should fail")
        } catch {
          case e: TestFailedException => e.getMessage() must equal("span, that has class \"foo\" should appear at most 3 times, but it appears 4 times")
        }
      }
    }
  }

}
