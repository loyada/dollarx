package info.dollarx

import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.{WebElement, NoSuchElementException, By, WebDriver}
import scala.collection.JavaConverters._

case class InBrowserFinder(driver: WebDriver) {

  def find(el: Path) = {
    try {
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
    } catch {
      case e: NoSuchElementException => throw new NoSuchElementException(s"could not find $el", e)
    }
  }

  def findAll(el: Path): List[WebElement] = {
    el.getUnderlyingSource match {
      case Some(e) => el.getXPath match {
        case Some(path) => (e findElements By.xpath(path)).asScala.toList
        case None => List(e)
      }
      case None => {
        el.getXPath match {
          case Some(path) => {
            val processedPath = processedPathForFind(path)
            (driver findElements By.xpath(processedPath)).asScala.toList
          }
          case None => throw new IllegalArgumentException("webel is empty") // should never happen
        }
      }
    }
  }

  def click(el: Path): WebElement = {
    val found = find(el)
    found.click()
    found
  }

  def doubleClick(el: Path): WebElement = {
    val found = find(el)
    found.click()
    found
  }

  def hoverOver(el: Path): WebElement = {
    val found = find(el)
    //  val mouse = driver.asInstanceOf[HasInputDevices].getMouse
    //  mouse.mouseMove(found.asInstanceOf[Locatable].getCoordinates)
    val actionBuilder = new Actions(driver)
    actionBuilder.moveToElement(found).build().perform()
    found
  }


  private def processedPathForFind(path: String) = {
    if (path.startsWith("not(.//")) {
      s"/html[.$path]"
    } else if (path.startsWith("not")) {
      val processedPath = path.replaceFirst("not[(]", "not(.//")
      s"/html[$processedPath]"
    } else (if (path.startsWith("/")) "" else "//") + path
  }
}


