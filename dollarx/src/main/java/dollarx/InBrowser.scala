package dollarx

import org.openqa.selenium.interactions.{Actions, HasInputDevices}
import org.openqa.selenium.internal.Locatable
import org.openqa.selenium.{WebElement, By, WebDriver}

object InBrowser {
  var driver: WebDriver = _

  def apply() = new InBrowserObj(driver)

  def click(el: WebEl) = {
    val browser = new InBrowserObj(driver)
    browser.click(el)
  }

  def find(el: WebEl): WebElement = {
    val browser = new InBrowserObj(driver)
    browser.find(el)
  }

  def hoverOver(el: WebEl): WebElement = {
    val browser = new InBrowserObj(driver)
    browser.hoverOver(el)
  }

  val verify = find _
}

case class InBrowserObj(driver: WebDriver) {
  def find(el: WebEl) = {
    el.getUnderlyingSource match {
      case Some(e) => el.getXPath match {
        case Some(path) => e findElement By.xpath(path)
        case None => e
      }
      case None => {
        el.getXPath match {
          case Some(path) => {
            val processedPath = processedPathForFind(path)
            driver findElement By.xpath(processedPath)
          }
          case None => throw new IllegalArgumentException("webel is empty") // should never happen
        }
      }
    }
  }



  def findAll(el: WebEl) = {
    el.getUnderlyingSource match {
      case Some(e) => el.getXPath match {
        case Some(path) => e findElements By.xpath(path)
        case None => e
      }
      case None => {
        el.getXPath match {
          case Some(path) => {
            val processedPath = processedPathForFind(path)
            driver findElements By.xpath(processedPath)
          }
          case None => throw new IllegalArgumentException("webel is empty") // should never happen
        }
      }
    }
  }

  def click(el: WebEl): WebElement = {
    val found = find(el)
    found.click()
    found
  }

  def hoverOver(el: WebEl): WebElement = {
    val found = find(el)
    //  val mouse = driver.asInstanceOf[HasInputDevices].getMouse
    //  mouse.mouseMove(found.asInstanceOf[Locatable].getCoordinates)
    val actionBuilder = new Actions(driver);
    actionBuilder.moveToElement(found).build().perform()
    found
  }

  private def processedPathForFind(path: String) = {
    if (path.startsWith("not(.//")) {
      s"/html[.$path]"
    } else if (path.startsWith("not")) {
      val processedPath = path.replaceFirst("not[(]", "not(.//")
      s"/html[$processedPath]"
    } else if (!path.startsWith("/")) "//" else path
  }
}

