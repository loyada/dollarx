package info.dollarx

import org.openqa.selenium.interactions.{Action, Actions, HasInputDevices}
import org.openqa.selenium._
import scala.collection.JavaConverters._

object InBrowser {
  var driver: WebDriver = _

  def apply() = new InBrowserObj(driver)

  object Predicates {
    def apearsNTimes(el: Path, n: Int): Boolean = {
      try {
        findAll(el).size == n
      } catch {
        case _: NoSuchElementException => n==0
      }
    }

    def isPresent(webEl: Path): Boolean = {
      try {
        find(webEl)
        true
      } catch {
        case _: NoSuchElementException => false
      }
    }

    def isEnabled(webEl: Path): Boolean = {
        find(webEl).isEnabled
    }

    def isSelected(webEl: Path): Boolean = {
      find(webEl).isSelected
    }

    def isDisplayed(webEl: Path): Boolean = {
      find(webEl).isDisplayed
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

    def on(webEl: Path) = {
      InBrowser().click(webEl)
    }

    def at(webEl: Path) = {
      preformActions((a: Actions) => a.moveToElement(webEl).click())
    }
  }

  object scroll {
    def to(webEl: Path) = {
      preformActions((a: Actions) => a.moveToElement(webEl))
    }
  }

  object doubleClick {
    def on(webEl: Path) = {
      preformActions((a: Actions) => a.doubleClick(webEl))
    }
  }

  case class sendKeys(charsToSend: CharSequence*) {
    def toBrowser() = {
      preformActions((a: Actions) => a.sendKeys(charsToSend: _*))
    }

    def to(el: Path) = {
      preformActions((a: Actions) => a.sendKeys(el, charsToSend: _*))
    }
  }

  case class pressKeyDown(theKey: Keys) {
    def inBrowser() {
      preformActions((a: Actions) => a.keyDown(theKey))
    }

    def whileFocusedOn(el: Path) = {
      preformActions((a: Actions) => a.keyDown(el, theKey))
    }
  }

  case class releaseKey(theKey: Keys) {
    def inBrowser() {
      preformActions((a: Actions) => a.keyUp(theKey))
    }

    def whileFocusedOn(el: Path) = {
      preformActions((a: Actions) => a.keyUp(el, theKey))
    }
  }


  object hover {
    def over(el: Path): WebElement = {
      InBrowser().hoverOver(el)
    }
  }

  case class offset(x:Int, y: Int)

  trait DragAndDropFrom {
    def to(to: Path)
    def to(to:offset)
  }

  def dragAndDrop(el: Path): DragAndDropFrom = {
      new DragAndDropFrom {
        private val from = el

        private def opSetup(f: (Actions => Actions)) {
          val builder = new Actions(driver)
          val actionsSetup = builder.clickAndHold(from)
          f(actionsSetup).build().perform()
        }

        override def to(to: Path) {
          opSetup((a: Actions) => a.moveToElement(to).release(to))
        }

        override def to(offset: offset): Unit = {
          opSetup((a: Actions) => a.moveByOffset(offset.x, offset.y).release())
        }
    }

  }

  def find(el: Path): WebElement = {
    InBrowser().find(el)
  }

  def findAll(el: Path): List[WebElement] = {
    InBrowser().findAll(el)
  }

  val verify = find _
}


case class InBrowserObj(driver: WebDriver) {
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

