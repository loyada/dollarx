package info.dollarx

import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.{WebElement, Keys, WebDriver}

object Operations {

  case class Click(driver: WebDriver) {
    def currentLocation() = {
      preformActions(driver, (a: Actions) => a.click())
    }

    def on(path: Path) = {
      val found = InBrowserFinder.find(driver, path)
      found.click()
      found
    }

    def at(path: Path) = {
      val webEl = InBrowserFinder.find(driver, path)
      preformActions(driver, (a: Actions) => a.moveToElement(webEl).click())
    }
  }

  case class  Scroll(driver: WebDriver) {
    def to(path: Path) = {
      preformActions(driver, (a: Actions) => a.moveToElement(InBrowserFinder.find(driver, path)))
    }
  }

  case class  DoubleClick(driver: WebDriver) {
    def on(path: Path) = {
      preformActions(driver, (a: Actions) => a.doubleClick(InBrowserFinder.find(driver, path)))
    }
  }

  case class sendKeys(driver: WebDriver, charsToSend: CharSequence*) {
    def toBrowser() = {
      preformActions(driver, (a: Actions) => a.sendKeys(charsToSend: _*))
    }

    def to(el: Path) = {
      preformActions(driver, (a: Actions) => a.sendKeys(el, charsToSend: _*))
    }
  }

  case class pressKeyDown(driver: WebDriver, theKey: Keys) {
    def inBrowser() {
      preformActions(driver, (a: Actions) => a.keyDown(theKey))
    }

    def whileFocusedOn(el: Path) = {
      preformActions(driver, (a: Actions) => a.keyDown(el, theKey))
    }
  }

  case class releaseKey(driver:WebDriver, theKey: Keys) {
    def inBrowser() {
      preformActions(driver, (a: Actions) => a.keyUp(theKey))
    }

    def whileFocusedOn(el: Path) = {
      preformActions(driver, (a: Actions) => a.keyUp(el, theKey))
    }
  }


  case class Hover(driver: WebDriver) {
    def over(el: Path): WebElement = {
      val found = InBrowserFinder.find(driver, el)
      //  val mouse = driver.asInstanceOf[HasInputDevices].getMouse
      //  mouse.mouseMove(found.asInstanceOf[Locatable].getCoordinates)
      val actionBuilder = new Actions(driver)
      actionBuilder.moveToElement(found).build().perform()
      found
    }
  }

  case class Offset(x:Int, y: Int)

  trait DragAndDropFrom {
    def to(to: Path)
    def to(x: Int, y: Int)
  }


  case class KeysSender(driver: WebDriver, charsToSend: CharSequence*) {
    def toBrowser() {
      preformActions(driver, (e: Actions) => e.sendKeys(charsToSend:_*))
    }

    def to(path: Path) {
      preformActions(driver, e => e.sendKeys(InBrowserFinder.find(driver, path), charsToSend:_*))
    }
  }

  case class KeysDown(driver: WebDriver, keysToSend: Keys) {
    def inBrowser() {
      preformActions(driver, a => a.keyDown(keysToSend))
    }

    def on(path: Path) {
      preformActions(driver, a => a.keyDown(InBrowserFinder.find(driver, path), keysToSend))
    }
  }

  case class ReleaseKey(driver: WebDriver, keysToSend: Keys)  {
    def inBrowser() {
      preformActions(driver, a => a.keyUp(keysToSend))
    }

    def on(path: Path) {
      preformActions(driver, a => a.keyUp(InBrowserFinder.find(driver, path), keysToSend))
    }
  }

  private def preformActions(driver: WebDriver, func: Actions => Actions) {
    val actionBuilder: Actions = new Actions(driver)
    func(actionBuilder).build.perform()
  }
}
