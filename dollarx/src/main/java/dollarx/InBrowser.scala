package dollarx

import org.openqa.selenium.interactions.{Action, Actions, HasInputDevices}
import org.openqa.selenium.{Keys, WebElement, By, WebDriver}
import scala.collection.JavaConverters._

object InBrowser {
  var driver: WebDriver = _

  def apply() = new InBrowserObj(driver)

  object Predicates {
    def isPresent(webEl: WebEl): Boolean = {
      try {
        find(webEl)
        true
      } catch {
        case _: Exception => false
      }
    }
  }

  private def preformActions(f: (Actions => Actions)) = {
    val actions = new Actions(driver)
    f(actions).build().perform()
  }

  object click {
    def currentLocation() = {
      preformActions((a: Actions) => a.click())
    }

    def on(webEl: WebEl) = {
      InBrowser().click(webEl)
    }

    def over(webEl: WebEl) = {
      preformActions((a: Actions) => a.moveToElement(webEl).click())
    }
  }

  object doubleClick {
    def on(webEl: WebEl) = {
      preformActions((a: Actions) => a.doubleClick(webEl))
    }
  }

  case class sendKeys(charsToSend: CharSequence*) {
    def toBrowser() = {
      preformActions((a: Actions) => a.sendKeys(charsToSend: _*))
    }

    def to(el: WebEl) = {
      preformActions((a: Actions) => a.sendKeys(el, charsToSend: _*))
    }
  }

  case class pressKeyDown(theKey: Keys) {
    def inBrowser() {
      preformActions((a: Actions) => a.keyDown(theKey))
    }

    def whileFocusedOn(el: WebEl) = {
      preformActions((a: Actions) => a.keyDown(el, theKey))
    }
  }

  case class releaseKey(theKey: Keys) {
    def inBrowser() {
      preformActions((a: Actions) => a.keyUp(theKey))
    }

    def whileFocusedOn(el: WebEl) = {
      preformActions((a: Actions) => a.keyUp(el, theKey))
    }
  }


  object hover {
    def over(el: WebEl): WebElement = {
      InBrowser().hoverOver(el)
    }
  }

  object dragAndDrop {

    trait DragAndDropFrom {
      def to(to: WebEl)

      def toOffset(x: Int, y: Int)
    }

    def from(el: WebEl): DragAndDropFrom = {
      new DragAndDropFrom {
        private val from = el

        private def opSetup(f: (Actions => Actions)) {
          val builder = new Actions(driver)
          val actionsSetup = builder.clickAndHold(from)
          f(actionsSetup).build().perform()
        }

        override def to(to: WebEl) {
          opSetup((a: Actions) => a.moveToElement(to).release(to))
        }

        override def toOffset(x: Int, y: Int): Unit = {
          opSetup((a: Actions) => a.moveByOffset(x, y).release())
        }
      }
    }
  }

  def find(el: WebEl): WebElement = {
    InBrowser().find(el)
  }

  def findAll(el: WebEl): List[WebElement] = {
    InBrowser().findAll(el)
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


  def findAll(el: WebEl): List[WebElement] = {
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

  def click(el: WebEl): WebElement = {
    val found = find(el)
    found.click()
    found
  }

  def doubleClick(el: WebEl): WebElement = {
    val found = find(el)
    found.click()
    found
  }

  def hoverOver(el: WebEl): WebElement = {
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

