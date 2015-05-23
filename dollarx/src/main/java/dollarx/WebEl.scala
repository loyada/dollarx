package dollarx

import org.openqa.selenium.{WebElement, By}

object WebEl {
  def body = new WebEl(xpath = Some("body"))

  def div = new WebEl(xpath = Some("div"))

  def list_item = new WebEl(xpath = Some("li"))

  def unordered_list = new WebEl(xpath = Some("ul"))

  def span = new WebEl(xpath = Some("span"))

  def anchor = new WebEl(xpath = Some("a"))

  def html = new WebEl(xpath = Some("html"))

  def button = new WebEl(xpath = Some("button"))

  def input = new WebEl(xpath = Some("input"))

  def header1 = new WebEl(xpath = Some("h1"))

  def header2 = new WebEl(xpath = Some("h2"))

  def header3 = new WebEl(xpath = Some("h3"))

  def header4 = new WebEl(xpath = Some("h4"))

  def header5 = new WebEl(xpath = Some("h5"))

  def anyHeader = header1 or header2 or header3 or header4 or header5

  def anyElement = new WebEl(xpath = Some("*"))

  def lastSibling(webEl: WebEl) = {
    if (!webEl.getXPath().isDefined) throw new IllegalArgumentException()
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "[last()]"))
  }

  def apply(path: String): WebEl = {
    new WebEl(xpath = Some(path))
  }

  implicit def webElementToWebel(we: WebElement): WebEl = {
    new WebEl(Some(we))
  }

  implicit def webElToWebElement(we: WebEl): WebElement = {
    we.getXPath() match {
      case Some(path) => InBrowser find we
      case None => we.getUnderlyingSource().get
    }
  }

}

class WebEl(underlyingSource: Option[WebElement] = None, xpath: Option[String] = None,
            elementProps: List[ElementProperties] = Nil) {

  import WebEl._

  def getXPath(): Option[String] = {
    if (!xpath.isDefined && elementProps.isEmpty) {
      None
    } else {
      val processedXpath = xpath.getOrElse("*")
      val props = elementProps.map(e => s"[$e]").mkString("")
      Some(processedXpath + props)
    }
  }

  def getUnderlyingSource(): Option[WebElement] = underlyingSource

  def apply(n: Int) = {
    new WebEl(underlyingSource, xpath, elementProps = elementProps :+ ElementProperties.raw(s"${n + 1}"))
  }

  def withProperties(props: ElementProperties*) = new WebEl(underlyingSource, xpath, elementProps ++ props)

  def click(): Unit = {
    InBrowser click this
  }

  def unary_!(): WebEl = new WebEl(underlyingSource, Some(XpathUtils.DoesNotExist(getXPath().getOrElse(""))))

  def or(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(underlyingSource, Some(s"*[self::${getXPath().get} | self::${webEl.getXPath.get}]"))
  }

  def find(): WebElement = {
    InBrowser find this
  }

  def withClass(cssClass: String): WebEl = {
    new WebEl(underlyingSource, xpath, elementProps :+ ElementProperties.hasClass(cssClass))
  }

  def withTextContaining(txt: String): WebEl = {
    new WebEl(underlyingSource, xpath, elementProps :+ ElementProperties.hasTextContaining(txt))
  }

  def withText(txt: String): WebEl = {
    new WebEl(underlyingSource, xpath, elementProps :+ ElementProperties.hasText(txt))
  }

  def withId(id: String): WebEl = {
    new WebEl(underlyingSource, xpath, elementProps :+ ElementProperties.hasId(id))
  }

  def inside(we: WebEl): WebEl = {
    new WebEl(we.getUnderlyingSource(), xpath = Some(we.getXPath().getOrElse("") + "//" + getXPath().getOrElse("")))
  }

  def childOf(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(getXPath().get + "/child::" + webEl.getXPath().get))
  }

  def parentOf(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(getXPath().get + "/parent::" + webEl.getXPath().get))
  }

  def containing(webEl: WebEl) = ancestorOf(webEl)
  def contains(webEl: WebEl) = descendantOf(webEl)

  def ancestorOf(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/ancestor::" + getXPath().get))
  }

  def descendantOf(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/descendant::" + getXPath().get))
  }

  def afterSibling(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/following-sibling::" + getXPath().get))
  }

  def after(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/following::" + getXPath().get))
  }

  def beforeSibling(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/preceding-sibling::" + getXPath().get))
  }

  def before(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/preceding::" + getXPath().get))
  }

  private def verifyRelationBetweenElements(webEl: WebEl) {
    if (webEl.getUnderlyingSource().isDefined || !getXPath().isDefined || !webEl.getXPath().isDefined) throw new IllegalArgumentException()
  }


  override def toString() = "xpath: " + getXPath().getOrElse("")
}
