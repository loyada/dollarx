package info.dollarx

import org.openqa.selenium.WebElement


object Path {

  val body = new Path(xpath = Some("body"), xpathExplanation = Some("body"))
  val div = new Path(xpath = Some("div"), xpathExplanation = Some("div"))
  val listItem = new Path(xpath = Some("li"), xpathExplanation = Some("list-item"))
  val unorderedList = new Path(xpath = Some("ul"), xpathExplanation = Some("unordered-list"))
  val span = new Path(xpath = Some("span"), xpathExplanation = Some("span"))
  val anchor = new Path(xpath = Some("a"), xpathExplanation = Some("a"))
  val html = new Path(xpath = Some("html"), xpathExplanation = Some("html"))
  val button = new Path(xpath = Some("button"), xpathExplanation = Some("button"))
  val input = new Path(xpath = Some("input"), xpathExplanation = Some("input"))
  val header1 = new Path(xpath = Some("h1"), xpathExplanation = Some("header1"))
  val header2 = new Path(xpath = Some("h2"), xpathExplanation = Some("header2"))
  val header3 = new Path(xpath = Some("h3"), xpathExplanation = Some("header3"))
  val header4 = new Path(xpath = Some("h4"), xpathExplanation = Some("header4"))
  val header5 = new Path(xpath = Some("h5"), xpathExplanation = Some("header5"))
  val header = header1 or header2 or header3 or header4 or header5
  val element = new Path(xpath = Some("*"), xpathExplanation = Some("element"))

  def last(path: Path) = {
    if (!path.getXPath().isDefined) throw new IllegalArgumentException()
    new Path(path.getUnderlyingSource(), xpath = path.getXPath,
      elementProps = path.getElementProperties :+ ElementProperties.lastSiblingOfType, xpathExplanation = Some(s"last ${path.toString}"))
  }

  def apply(path: String): Path = {
    new Path(xpath = Some(path))
  }

  implicit def webElementToWebel(we: WebElement): Path = {
    new Path(Some(we))
  }

  implicit def webElToWebElement(path: Path): WebElement = {
    path.getXPath() match {
      case None => path.getUnderlyingSource().get
      case _ => InBrowser find path

    }
  }


  implicit def intToNWebEl(n: Int) = {

  }

}

case class NWebEl(n: Int, el: Path)


class Path(underlyingSource: Option[WebElement] = None, xpath: Option[String] = None, insideXpath: Option[String] = None,
            elementProps: List[ElementProperties] = Nil, xpathExplanation: Option[String] = None, describedBy: Option[String] = None) {


  def getXPath(): Option[String] = {
    if (!xpath.isDefined && elementProps.isEmpty && insideXpath.isEmpty) {
      None
    } else {
      val processedXpath = (if (insideXpath.isDefined) (insideXpath.get + "//") else "") + xpath.getOrElse("*")
      val props = elementProps.map(e => s"[${e.toXpath()}]").mkString("")
      Some(processedXpath + props)
    }
  }

  private def getXPathWithoutInsideClause: Option[String] = {
    if (xpath.isEmpty && elementProps.isEmpty) {
      None
    }
    else {
      val props: String = elementProps.map(e => s"[${e.toXpath}]").mkString("")
      Some(xpath.getOrElse("*") + props)
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
      new Path(underlyingSource, xpath, elementProps = elementProps :+ prop, xpathExplanation = xpathExplanation)
    } else {
      new Path(underlyingSource, getXPath(), elementProps = List(prop), xpathExplanation = this.describedBy, describedBy = this.describedBy)
    }
  }

  def that(props: ElementProperties*): Path = {
    if (describedBy.isDefined) {
      new Path(underlyingSource, xpath= getXPathWithoutInsideClause, elementProps = List(props:_*), xpathExplanation = describedBy, insideXpath = insideXpath)
    } else {
      new Path(underlyingSource, xpath = xpath, elementProps = elementProps ++ props, xpathExplanation = xpathExplanation, insideXpath = insideXpath)
    }
  }
  

  def unary_!(): Path = new Path(underlyingSource, Some(XpathUtils.DoesNotExistInEntirePage(getXPath().getOrElse(""))), xpathExplanation = Some(s"anything except (${toString()})"))

  def or(path: Path) = {
    verifyRelationBetweenElements(path)
    new Path(underlyingSource, Some(s"*[self::${getXPath().get} | self::${path.getXPath.get}]"), xpathExplanation = Some(s"(${wrapIfNeeded(this)}) or (${wrapIfNeeded(path)})"))
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
      new Path(underlyingSource, xpath, elementProps = elementProps :+ prop, insideXpath = insideXpath, xpathExplanation = xpathExplanation)
    } else {
      new Path(underlyingSource, getXPath(), insideXpath = insideXpath, elementProps = List(prop), xpathExplanation = describedBy, describedBy = describedBy)
    }
  }

  def withText(txt: String): Path = {
    createNewWithAdditionalProperty(ElementProperties.hasText(txt))
  }

  def withId(id: String): Path = {
    createNewWithAdditionalProperty(ElementProperties.hasId(id))
  }

  def inside(path: Path): Path = {
    verifyRelationBetweenElements(path)
    new Path(path.getUnderlyingSource(),
      xpath = Some(getXPathWithoutInsideClause.getOrElse("")),
      insideXpath = Some(getXPath().get + (if (insideXpath.isDefined)  ("//" + insideXpath.get) else "")),
      xpathExplanation = Some(toString + s", inside (${wrapIfNeeded(path)})"))
  }

  private def wrapIfNeeded(path: Path): String = {
    return if ((path.toString.trim.contains(" "))) "(" + path + ")" else path.toString
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

  private def createWithSimpleRelation(path: Path, relation: String): Path = {
    verifyRelationBetweenElements(path)
    val myXpath: String = getXPath.get
    val isInside: Boolean = insideXpath.isDefined
    val processedXpath: String = if (isInside) s"*[ancestor::${insideXpath.get} and self::${xpath.getOrElse("*")}]" else myXpath
    new Path(underlyingSource = underlyingSource,
      xpath = Some(path.getXPath.get + "/" + relation + "::" + processedXpath),
      xpathExplanation = Some(toString + ", " + relation + " of " + path.toString))
  }

  private def createWithHumanReadableRelation(path: Path, xpathRelation: String, humanReadableRelation: String): Path = {
    verifyRelationBetweenElements(path)
    val myXpath: String = getXPath.get
    val isInside: Boolean = insideXpath.isDefined
    val processedXpath: String = if (isInside) s"*[ancestor::${insideXpath.get}]" else myXpath
    new Path(underlyingSource = underlyingSource,
      xpath = Some(path.getXPath.get + "/" + xpathRelation + "::" + processedXpath),
      xpathExplanation = Some(toString + ", " + humanReadableRelation + " " + wrapIfNeeded(path)))
  }

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



