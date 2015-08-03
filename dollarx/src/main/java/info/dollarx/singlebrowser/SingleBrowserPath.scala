package info.dollarx.singlebrowser

import info.dollarx.Path

import org.openqa.selenium.WebElement


object SingleBrowserPath {
  var browser: SingleBrowser = _
  implicit def singleBrowserPathToWebElement(path: SingleBrowserPath): WebElement = {
    path.getXPath match {
      case None => path.getUnderlyingSource().get
      case _ =>  path.find()
    }
  }

}

class SingleBrowserPath extends Path {
  def  browser = SingleBrowserPath.browser
  def find(): WebElement = {
     browser find this
  }


  def findAll: List[WebElement] = {
     browser findAll this
  }

  def scrollTo(): Unit = {
     browser.scroll to this
  }

   def hover() {
    browser.hover over this
  }

   def click() {
    browser.click().at(this)
  }

  def doubleClick() {
    browser.doubleClick on this
  }

  def dragAndDrop() = {
    browser dragAndDrop this
  }

  def sendKeys(charsToSend: CharSequence*) {
    browser.sendKeys(charsToSend:_*).to(this)
  }
}
