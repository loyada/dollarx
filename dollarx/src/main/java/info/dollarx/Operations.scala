package info.dollarx


import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.{JavascriptExecutor, WebElement, Keys, WebDriver}

class OperationFailedException(reason: String, cause: Throwable) extends Exception(reason, cause)

object Operations {

  case class Click(driver: WebDriver) {
    def currentLocation() = {
      preformActions(driver, (a: Actions) => a.click())
    }

    def on(path: Path) = {
      try {
        val found = InBrowserFinder.find(driver, path)
        found.click()
        found
      } catch {
        case ex: Exception => throw new OperationFailedException("could not click on " + path, ex)
      }
    }

    def at(path: Path) = {
      try {
        val webEl = InBrowserFinder.find(driver, path)
        preformActions(driver, (a: Actions) => a.moveToElement(webEl).click())
      } catch {
        case ex: Exception => throw new OperationFailedException("could not click at " + path, ex)
      }
    }
  }

  case class Scroll(driver: WebDriver) {
    def to(path: Path) = {
      preformActions(driver, (a: Actions) => a.moveToElement(InBrowserFinder.find(driver, path)))
    }

    private def scrollInternal(x: Int, y: Int): Unit = {
      (driver.asInstanceOf[JavascriptExecutor]).executeScript(s"scroll($x,$y)")

    }
    def left(n: Int) {
      scrollInternal(-1 * n, 0)
    }
    def right(n: Int) {
      scrollInternal(n, 0)
    }
    def up(n: Int) {
      scrollInternal(0, -1 * n)
    }
    def down(n: Int) {
      scrollInternal(0, n)
    }

  }

  case class DoubleClick(driver: WebDriver) {
    def on(path: Path) = {
      try {
        preformActions(driver, (a: Actions) => a.doubleClick(InBrowserFinder.find(driver, path)))
      } catch {
        case ex: Exception => throw new OperationFailedException("could not double-click at " + path, ex)
      }
    }
  }

  case class SendKeys(driver: WebDriver, charsToSend: CharSequence*) {
    def toBrowser() = {
      preformActions(driver, (a: Actions) => a.sendKeys(charsToSend: _*))
    }

    def to(el: Path) = {
      try {
        val found = InBrowserFinder.find(driver, el)
        preformActions(driver, (a: Actions) => a.sendKeys(found, charsToSend: _*))
      } catch {
        case ex: Exception => throw new OperationFailedException("could not send keys to " + el, ex)
      }
    }
  }

  case class PressKeyDown(driver: WebDriver, theKey: Keys) {
    def inBrowser() {
      preformActions(driver, (a: Actions) => a.keyDown(theKey))
    }

    def whileFocusedOn(el: Path) = {
      try {
        val found = InBrowserFinder.find(driver, el)
        preformActions(driver, (a: Actions) => a.keyDown(found, theKey))
      } catch {
        case ex: Exception => throw new OperationFailedException("could not press key down while focused on  " + el, ex)
      }
    }
  }

  case class ReleaseKey(driver: WebDriver, theKey: Keys) {
    def inBrowser() {
      preformActions(driver, (a: Actions) => a.keyUp(theKey))
    }

    def whileFocusedOn(el: Path) = {
      try {
        val found = InBrowserFinder.find(driver, el)
        preformActions(driver, (a: Actions) => a.keyUp(found, theKey))
      } catch {
        case ex: Exception => throw new OperationFailedException("could not release keys while focused on  " + el, ex)
      }
    }
  }


  case class Hover(driver: WebDriver) {
    def over(el: Path): WebElement = {
      try {
        val found = InBrowserFinder.find(driver, el)
        //  val mouse = driver.asInstanceOf[HasInputDevices].getMouse
        //  mouse.mouseMove(found.asInstanceOf[Locatable].getCoordinates)
        val actionBuilder = new Actions(driver)
        actionBuilder.moveToElement(found).build().perform()
        found
      } catch {
        case ex: Exception => throw new OperationFailedException("could not hover over " + el, ex)
      }
    }
  }

  case class Offset(x: Int, y: Int)

  trait DragAndDropFrom {
    def to(to: Path)
    def to(x: Int, y: Int)
  }


  case class KeysSender(driver: WebDriver, charsToSend: CharSequence*) {
    def toBrowser() {
      preformActions(driver, (e: Actions) => e.sendKeys(charsToSend: _*))
    }

    def to(path: Path) {
      try {
        preformActions(driver, e => e.sendKeys(InBrowserFinder.find(driver, path), charsToSend: _*))
      } catch {
        case ex: Exception => throw new OperationFailedException("could not send keys to " + path, ex)
      }
    }
  }

  case class KeysDown(driver: WebDriver, keysToSend: Keys) {
    def inBrowser() {
      preformActions(driver, a => a.keyDown(keysToSend))
    }

    def on(path: Path) {
      try {
        preformActions(driver, a => a.keyDown(InBrowserFinder.find(driver, path), keysToSend))
      } catch {
        case ex: Exception => throw new OperationFailedException("could not send keys to " + path, ex)
      }
    }
  }

  private def preformActions(driver: WebDriver, func: Actions => Actions) {
    val actionBuilder: Actions = new Actions(driver)
    func(actionBuilder).build.perform()
  }
}
