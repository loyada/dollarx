package dollarx

import org.openqa.selenium.{WebElement, By}


object Path {

  def body = new Path(xpath = Some("body"), xpathExplanation = Some("body"))

  def div = new Path(xpath = Some("div"), xpathExplanation = Some("div"))

  def listItem = new Path(xpath = Some("li"), xpathExplanation = Some("list-item"))

  def unorderedList = new Path(xpath = Some("ul"), xpathExplanation = Some("unordered-list"))

  def span = new Path(xpath = Some("span"), xpathExplanation = Some("span"))

  def anchor = new Path(xpath = Some("a"), xpathExplanation = Some("a"))

  def html = new Path(xpath = Some("html"), xpathExplanation = Some("html"))

  def button = new Path(xpath = Some("button"), xpathExplanation = Some("button"))

  def input = new Path(xpath = Some("input"), xpathExplanation = Some("input"))

  def header1 = new Path(xpath = Some("h1"), xpathExplanation = Some("header1"))

  def header2 = new Path(xpath = Some("h2"), xpathExplanation = Some("header2"))

  def header3 = new Path(xpath = Some("h3"), xpathExplanation = Some("header3"))

  def header4 = new Path(xpath = Some("h4"), xpathExplanation = Some("header4"))

  def header5 = new Path(xpath = Some("h5"), xpathExplanation = Some("header5"))

  def header = header1 or header2 or header3 or header4 or header5

  def element = new Path(xpath = Some("*"), xpathExplanation = Some("element"))

  def last(webEl: Path) = {
    if (!webEl.getXPath().isDefined) throw new IllegalArgumentException()
    new Path(webEl.getUnderlyingSource(), xpath = webEl.getXPath,
      elementProps = webEl.getElementProperties :+ ElementProperties.lastSiblingOfType, xpathExplanation = Some(s"last ${webEl.toString}"))
  }

  def apply(path: String): Path = {
    new Path(xpath = Some(path))
  }

  implicit def webElementToWebel(we: WebElement): Path = {
    new Path(Some(we))
  }

  implicit def webElToWebElement(we: Path): WebElement = {
    we.getXPath() match {
      case Some(path) => InBrowser find we
      case None => we.getUnderlyingSource().get
    }
  }


  implicit def intToNWebEl(n: Int) = {

  }

}

case class NWebEl(n: Int, el: Path)


class Path(underlyingSource: Option[WebElement] = None, xpath: Option[String] = None,
            elementProps: List[ElementProperties] = Nil, xpathExplanation: Option[String] = None, describedBy: Option[String] = None) {

  import Path._

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

  def getElementProperties = elementProps

  def apply(n: Int) = {
    var prop = new ElementProperties {
      override def toXpath(): String = s"${n + 1}"

      override def toString: String = "with index " + n
    }
    if (this.describedBy.isEmpty) {
      new Path(underlyingSource, xpath, elementProps = elementProps :+ prop, xpathExplanation)
    } else {
      new Path(underlyingSource, getXPath(), List(prop), xpathExplanation = this.describedBy, describedBy = this.describedBy)
    }
  }

  def that(props: ElementProperties*): Path = new Path(underlyingSource, xpath, elementProps ++ props, xpathExplanation)
  

  def unary_!(): Path = new Path(underlyingSource, Some(XpathUtils.DoesNotExistInEntirePage(getXPath().getOrElse(""))), xpathExplanation = Some(s"anything except (${toString()})"))

  def or(webEl: Path) = {
    verifyRelationBetweenElements(webEl)
    new Path(underlyingSource, Some(s"*[self::${getXPath().get} | self::${webEl.getXPath.get}]"), xpathExplanation = Some(s"(${toString()}) or ($webEl)"))
  }

  def find(): WebElement = {
    InBrowser find this
  }

  def withClass(cssClass: String): Path = {
    createNewWithAdditionalProperty(ElementProperties.hasClass(cssClass))
  }

  def withClasses(cssClasses: String*): Path = {
    createNewWithAdditionalProperty(ElementProperties.hasClasses(cssClasses: _*))
  }

  def withTextContaining(txt: String): Path = {
    createNewWithAdditionalProperty(ElementProperties.hasTextContaining(txt))
  }

  private def createNewWithAdditionalProperty( prop: ElementProperties) = {
    if (describedBy.isEmpty) {
      new Path(underlyingSource, xpath, elementProps :+ prop, xpathExplanation)
    } else {
      new Path(underlyingSource, getXPath(), List(prop), xpathExplanation = describedBy, describedBy = describedBy)
    }
  }

  def withText(txt: String): Path = {
    createNewWithAdditionalProperty(ElementProperties.hasText(txt))
  }

  def withId(id: String): Path = {
    createNewWithAdditionalProperty(ElementProperties.hasId(id))
  }

  def inside(webEl: Path): Path = {
    verifyRelationBetweenElements(webEl)
    new Path(webEl.getUnderlyingSource(), xpath = Some(webEl.getXPath().getOrElse("") + "//" + getXPath().getOrElse("")), xpathExplanation = Some(toString + s", inside ($webEl)"))
  }

  def childOf(webEl: Path) = {
    verifyRelationBetweenElements(webEl)
    new Path(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/child::" + getXPath().get), xpathExplanation = Some(toString() + s", child of ($webEl)"))
  }

  def parentOf(webEl: Path) = {
    verifyRelationBetweenElements(webEl)
    new Path(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/parent::" + getXPath().get), xpathExplanation = Some(toString() + s", parent of ($webEl)"))
  }

  def containing(webEl: Path) = ancestorOf(webEl)

  def containedIn(webEl: Path) = descendantOf(webEl)

  def ancestorOf(webEl: Path) = {
    verifyRelationBetweenElements(webEl)
    new Path(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/ancestor::" + getXPath().get), xpathExplanation = Some(toString() + s", ancestor of ($webEl)"))

  }

  def descendantOf(webEl: Path) = {
    verifyRelationBetweenElements(webEl)
    new Path(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/descendant::" + getXPath().get), xpathExplanation = Some(toString() + s", descendent of ($webEl)"))
  }

  def afterSibling(webEl: Path) = {
    verifyRelationBetweenElements(webEl)
    new Path(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/following-sibling::" + getXPath().get), xpathExplanation = Some(toString() + s", after sibling ($webEl)"))
  }

  def after(webEl: Path) = {
    verifyRelationBetweenElements(webEl)
    new Path(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/following::" + getXPath().get), xpathExplanation = Some(toString() + s", after ($webEl)"))
  }

  def beforeSibling(webEl: Path) = {
    verifyRelationBetweenElements(webEl)
    new Path(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/preceding-sibling::" + getXPath().get), xpathExplanation = Some(toString() + s", before sibling ($webEl)"))
  }

  def before(webEl: Path) = {
    verifyRelationBetweenElements(webEl)
    new Path(webEl.getUnderlyingSource(), Some(webEl.getXPath().get + "/preceding::" + getXPath().get), xpathExplanation = Some(toString() + s", before ($webEl)"))
  }

  private def verifyRelationBetweenElements(webEl: Path) {
    if (webEl.getUnderlyingSource().isDefined || !getXPath().isDefined || !webEl.getXPath().isDefined) throw new IllegalArgumentException()
  }

  def describedBy(txt: String) = {
    val descriptionAsOption = Some(txt)
    new Path(underlyingSource, xpath = xpath, elementProps = elementProps, xpathExplanation = xpathExplanation, describedBy = descriptionAsOption)
  }

  def toXpath() = "xpath: " + getXPath().getOrElse("")

  override def toString() = {
    if (describedBy.isDefined && describedBy != xpathExplanation) {
      describedBy.get
    } else {
      val underlyingOption = if (underlyingSource.isDefined) Some(s"under reference element ${underlyingSource.get}") else None
      val xpathOption = if (xpath.isDefined) {
        xpathExplanation match {
          case Some(s) => Some(s)
          case _ => Some( s"""xpath: "${xpath.get}"""")
        }
      } else None

      val propsOption = if (elementProps.size == 1 && (!xpathOption.getOrElse("").contains(" ") || xpathOption == describedBy)) {
        val thatOption = if (elementProps.head.toString.startsWith("has")) "that " else ""
        Some(s"$thatOption${elementProps.head}")
      } else if (elementProps.size == 2 && !xpathOption.getOrElse("").contains(" ")) {
        Some(s"that ${elementProps.head}, and ${elementProps.last}")
      } else if (elementProps.size > 1 || (xpathOption.getOrElse("").contains(" ") && !elementProps.isEmpty)) {
        val propsAsList = elementProps.map(p => p.toString).mkString(", ")
        if (xpathExplanation.isDefined && xpathExplanation.get.contains("with properties") || elementProps.size == 1) {
          Some(s"and $propsAsList")
        } else {
          Some(s"with properties [$propsAsList]")
        }
      } else None

      val detail = if (xpathExplanation.isDefined && !underlyingOption.isDefined && !propsOption.isDefined) {
        xpathExplanation.get
      } else {
        List(underlyingOption, xpathOption, propsOption).flatten.mkString(", ")
      }
      s"$detail"
    }
  }


}



