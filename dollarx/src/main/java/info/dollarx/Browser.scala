package info.dollarx

import info.dollarx.Operations._
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium._

trait Browser {
   protected val driver: WebDriver

  def find(el: Path): WebElement = {
     InBrowserFinder.find(driver, el)
  }

  def findPageWithNumberOfOccurrences(el: Path, NumberOfOccurances: Int): WebElement = {
     InBrowserFinder.findPageWithNumberOfOccurrences(driver, el, NumberOfOccurances)
  }

  def findPageWithout(el: Path): WebElement = {
     InBrowserFinder.findPageWithout(driver, el)
  }

  def findAll(el: Path): List[WebElement] = {
     InBrowserFinder.findAll(driver, el)
  }

  def numberOfAppearances(el: Path): Integer = {
     InBrowserFinder.findAll(driver, el).size
  }

  def isPresent(el: Path): Boolean = {
    try {
      find(el)
       true
    }
    catch {
      case e: NoSuchElementException => false
    }
  }

  def isNotPresent(el: Path): Boolean = {
    try {
      findPageWithout(el)
       true
    }
    catch {
      case e: NoSuchElementException => false
    }
  }

  def isEnabled(el: Path): Boolean = {
    try {
       find(el).isEnabled
    }
    catch {
      case e: NoSuchElementException => false
    }
  }

  def isSelected(el: Path): Boolean = {
    try {
       find(el).isSelected
    }
    catch {
      case e: NoSuchElementException => false
    }
  }

  def isDisplayed(el: Path): Boolean = {
    try {
       find(el).isDisplayed
    }
    catch {
      case e: NoSuchElementException => false
    }
  }


  def click(): Click = Click(driver)

  def doubleClick: DoubleClick = DoubleClick(driver)

  def hover: Hover = Hover(driver)

  def scroll: Scroll = Scroll(driver)

  def sendKeys(charsToSend: CharSequence*): KeysSender = KeysSender(driver, charsToSend:_*)

  def pressKeyDown(theKey: Keys): Operations.KeysDown = {
      Operations.KeysDown(driver, theKey)
  }

  def releaseKey(theKey: Keys): Operations.ReleaseKey = {
      Operations.ReleaseKey(driver, theKey)
  }

  def dragAndDrop(el: Path): DragAndDropFrom = {
    new DragAndDropFrom {
       val from = el

      private def opSetup(f: (Actions => Actions)) {
        val builder = new Actions(driver)
        val actionsSetup = builder.clickAndHold(from)
        f(actionsSetup).build().perform()
      }

      override def to(to: Path) {
        opSetup((a: Actions) => a.moveToElement(to).release(to))
      }

      override def to(offset: Offset): Unit = {
        opSetup((a: Actions) => a.moveByOffset(offset.x, offset.y).release())
      }
    }
  }
}
