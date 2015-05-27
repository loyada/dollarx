package dollarx

import com.gargoylesoftware.htmlunit.ElementNotFoundException
import dollarx.ElementProperties.{hasText, hasClass}
import org.openqa.selenium.{By, WebDriver}
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

  def doubleYourPleasure(i: Int): Int = i * 2

  describe("Browser") {

   /* it("should pop values in last-in-first-out order") {
      val evenNum = 2
      evenNum must be(even)
      doubleYourPleasure(evenNum) must be(even)

      val oddNum = 3
      oddNum must be(odd)
      doubleYourPleasure(oddNum) must be(odd) // This will fail    }
    }*/
    it("should check for presence correctly") {
      val el = div withClass("foo")
      val xpath = el.getXPath().get
      when(InBrowser.driver.findElement(By.xpath("//" + xpath))).thenThrow(new ElementNotFoundException("", "", ""))
      el must be(present)
    }
    it("should check for absence correctly") {
      when(InBrowser.driver.findElement(By.xpath("/html[not(.//div)]"))).thenThrow(new ElementNotFoundException("", "", ""))
      div mustNot be(absent)
    }
  }
}