package dollarx

import org.openqa.selenium.{WebElement, By}


object WebEl {
  def body = new WebEl(xpath = Some("body"), xpathExplanation = Some("body"))

  def div = new WebEl(xpath = Some("div"), xpathExplanation = Some("div"))

  def list_item = new WebEl(xpath = Some("li"), xpathExplanation = Some("list-item"))

  def unordered_list = new WebEl(xpath = Some("ul"), xpathExplanation = Some("unordered-list"))

  def span = new WebEl(xpath = Some("span"), xpathExplanation = Some("span"))

  def anchor = new WebEl(xpath = Some("a"), xpathExplanation = Some("a"))

  def html = new WebEl(xpath = Some("html"), xpathExplanation = Some("html"))

  def button = new WebEl(xpath = Some("button"), xpathExplanation = Some("button"))

  def input = new WebEl(xpath = Some("input"), xpathExplanation = Some("input"))

  def header1 = new WebEl(xpath = Some("h1"), xpathExplanation = Some("header1"))

  def header2 = new WebEl(xpath = Some("h2"), xpathExplanation = Some("header2"))

  def header3 = new WebEl(xpath = Some("h3"), xpathExplanation = Some("header3"))

  def header4 = new WebEl(xpath = Some("h4"), xpathExplanation = Some("header4"))

  def header5 = new WebEl(xpath = Some("h5"), xpathExplanation = Some("header5"))

  def anyHeader = header1 or header2 or header3 or header4 or header5

  def anyElement = new WebEl(xpath = Some("*"), xpathExplanation = Some("any"))

  def lastSibling(webEl: WebEl) = {
    if (!webEl.getXPath().isDefined) throw new IllegalArgumentException()
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "[last()]"), xpathExplanation = Some("last " + webEl))
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
            elementProps: List[ElementProperties] = Nil, xpathExplanation: Option[String] = None) {

  import WebEl._

  def getXPath(): Option[String] = {
    if (!xpath.isDefined && elementProps.isEmpty) {
      None
    } else {
      val processedXpath = xpath.getOrElse("*")
      val props = elementProps.map(e => s"[${e.toXpath()}]").mkString("")
      Some(processedXpath + props)
    }
  }

  def getUnderlyingSource(): Option[WebElement] = underlyingSource

  def apply(n: Int) = {
    new WebEl(underlyingSource, xpath, elementProps = elementProps :+ ElementProperties.index(n), xpathExplanation)
  }

  def withProperties(props: ElementProperties*) = new WebEl(underlyingSource, xpath, elementProps ++ props, xpathExplanation)

  def click(): Unit = {
    InBrowser.click on this
  }

  def unary_!(): WebEl = new WebEl(underlyingSource, Some(XpathUtils.DoesNotExist(getXPath().getOrElse(""))), xpathExplanation = Some(s"anything except (${toString()})"))

  def or(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(underlyingSource, Some(s"*[self::${getXPath().get} | self::${webEl.getXPath.get}]"), xpathExplanation = Some(s"(${toString()}) or ($webEl)"))
  }

  def find(): WebElement = {
    InBrowser find this
  }

  def withClass(cssClass: String): WebEl = {
    new WebEl(underlyingSource, xpath, elementProps :+ ElementProperties.hasClass(cssClass), xpathExplanation)
  }

  def withTextContaining(txt: String): WebEl = {
    new WebEl(underlyingSource, xpath, elementProps :+ ElementProperties.hasTextContaining(txt), xpathExplanation)
  }

  def withText(txt: String): WebEl = {
    new WebEl(underlyingSource, xpath, elementProps :+ ElementProperties.hasText(txt),  xpathExplanation)
  }

  def withId(id: String): WebEl = {
    new WebEl(underlyingSource, xpath, elementProps :+ ElementProperties.hasId(id), xpathExplanation)
  }

  def inside(webEl: WebEl): WebEl = {
    new WebEl(webEl.getUnderlyingSource(), xpath = Some(webEl.getXPath().getOrElse("") + "//" + getXPath().getOrElse("")), xpathExplanation = Some(toString + s", inside ($webEl)"))
  }

  def childOf(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(getXPath().get + "/child::" + webEl.getXPath().get), xpathExplanation = Some(toString() + s", child of ($webEl)"))
  }

  def parentOf(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(getXPath().get + "/parent::" + webEl.getXPath().get), xpathExplanation = Some(toString() + ", parent of ($webEl)"))
  }

  def containing(webEl: WebEl) = ancestorOf(webEl)

  def contains(webEl: WebEl) = descendantOf(webEl)

  def ancestorOf(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/ancestor::" + getXPath().get), xpathExplanation = Some(toString() + s", ancestor of ($webEl)"))

  }

  def descendantOf(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/descendant::" + getXPath().get), xpathExplanation = Some(toString() + s", descendent of ($webEl)"))
  }

  def afterSibling(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/following-sibling::" + getXPath().get), xpathExplanation = Some(toString() + s", after sibling ($webEl)"))
  }

  def after(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/following::" + getXPath().get), xpathExplanation = Some(toString() + s", after ($webEl)"))
  }

  def beforeSibling(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/preceding-sibling::" + getXPath().get), xpathExplanation = Some(toString() + s", before sibling ($webEl)"))
  }

  def before(webEl: WebEl) = {
    verifyRelationBetweenElements(webEl)
    new WebEl(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/preceding::" + getXPath().get), xpathExplanation = Some(toString() + s", before ($webEl)"))
  }

  private def verifyRelationBetweenElements(webEl: WebEl) {
    if (webEl.getUnderlyingSource().isDefined || !getXPath().isDefined || !webEl.getXPath().isDefined) throw new IllegalArgumentException()
  }

  def toXpath() = "xpath: " + getXPath().getOrElse("")

  override def toString() = {
    val underlyingOption = if (underlyingSource.isDefined) Some(s"under reference element ${underlyingSource.get}") else None
    val xpathOption = if (xpath.isDefined) {
      xpathExplanation match {
        case Some(s) => Some(s)
        case _ => Some( s"""xpath: "${xpath.get}"""")
      }
    } else None

    val propsOption = if (elementProps.size > 0) Some(s"with properties:[${elementProps.map(p => p.toString).mkString(", ")}]") else None

    val detail = if (xpathExplanation.isDefined && !underlyingOption.isDefined && !propsOption.isDefined) {
      xpathExplanation.get
    } else {
       List(underlyingOption, xpathOption, propsOption).flatten.mkString(", ")
    //  val translationToXpath = if (propsOption.isDefined) s""", full xpath is: "${getXPath().get}"""" else ""
    }
    s"$detail"

  }
}
