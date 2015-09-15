package info.dollarx.singlebrowser

import info.dollarx.{ElementProperty, Path}

import org.openqa.selenium.WebElement


object SingleBrowserPath {
  private var myBrowser:Option[SingleBrowser] = None
  def browser: SingleBrowser = {
    if (myBrowser.isEmpty) myBrowser = Some(new SingleBrowser)
    myBrowser.get
  }

  implicit def singleBrowserPathToWebElement(path: SingleBrowserPath): WebElement = {
    path.getXPath match {
      case None => path.getUnderlyingSource().get
      case _ =>  path.find()
    }
  }

  implicit def pathToSingleBrowserPath(path: Path): SingleBrowserPath = {
      new SingleBrowserPath(
        xpath = path.xpath,
        insideXpath = path.insideXpath,
        elementProps = path.elementProps,
        xpathExplanation = path.xpathExplanation,
        describedBy = path.describedBy)
  }
}

class SingleBrowserPath(
                         underlyingSource: Option[WebElement] = None,
                         xpath: Option[String] = None,
                         insideXpath: Option[String] = None,
                         elementProps: List[ElementProperty] = Nil,
                         xpathExplanation: Option[String] = None,
                         describedBy: Option[String] = None
                         ) extends Path(underlyingSource, xpath, insideXpath, elementProps, xpathExplanation, describedBy ) {

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
