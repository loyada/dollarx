package info.dollarx

import org.openqa.selenium.WebElement


object Path {

  val body = new Path(xpath = Some("body"), xpathExplanation = Some("document body"))
  val div = new Path(xpath = Some("div"), xpathExplanation = Some("div"))
  val listItem = new Path(xpath = Some("li"), xpathExplanation = Some("list item"))
  val unorderedList = new Path(xpath = Some("ul"), xpathExplanation = Some("unordered-list"))
  val orderedList = new Path(xpath = Some("ol"), xpathExplanation = Some("ordered-list"))
  val span = new Path(xpath = Some("span"), xpathExplanation = Some("span"))
  val anchor = new Path(xpath = Some("a"), xpathExplanation = Some("anchor"))
  val image = new Path(xpath = Some("img"), xpathExplanation = Some("image"))
  val html = new Path(xpath = Some("html"), xpathExplanation = Some("document"))
  val button = new Path(xpath = Some("button"), xpathExplanation = Some("button"))
  val input = new Path(xpath = Some("input"), xpathExplanation = Some("input"))
  val form = new Path(xpath = Some("form"), xpathExplanation = Some("form"))
  val iframe = new Path(xpath = Some("iframe"), xpathExplanation = Some("iframe"))
  val title = new Path(xpath = Some("title"), xpathExplanation = Some("title"))
  val header1 = new Path(xpath = Some("h1"), xpathExplanation = Some("header1"))
  val header2 = new Path(xpath = Some("h2"), xpathExplanation = Some("header2"))
  val header3 = new Path(xpath = Some("h3"), xpathExplanation = Some("header3"))
  val header4 = new Path(xpath = Some("h4"), xpathExplanation = Some("header4"))
  val header5 = new Path(xpath = Some("h5"), xpathExplanation = Some("header5"))
  val header = header1 or header2 or header3 or header4 or header5
  val element = new Path(xpath = Some("*"), xpathExplanation = Some("any element"))

  def last(path: Path) = {
    if (path.getXPath.isEmpty) throw new IllegalArgumentException()
    new Path(path.getUnderlyingSource(), xpath = path.getXPath,
      elementProps = path.getElementProperties :+ ElementProperties.lastSiblingOfType, xpathExplanation = Some(s"last ${path.toString}"))
  }

  def apply(path: String): Path = {
    new Path(xpath = Some(path))
  }

  implicit def webElementToPath(we: WebElement): Path = {
    new Path(Some(we))
  }

  object first {
    def occurrenceOf(path: Path): Path = path(0)
  }
}




class Path(underlyingSource: Option[WebElement] = None, xpath: Option[String] = None, insideXpath: Option[String] = None,
            elementProps: List[ElementProperty] = Nil, xpathExplanation: Option[String] = None, describedBy: Option[String] = None) {


  val getXPath: Option[String] = {
    if (xpath.isEmpty && elementProps.isEmpty && insideXpath.isEmpty) {
      None
    } else {
      val processedXpath = (if (insideXpath.isDefined) (insideXpath.get + "//") else "") + xpath.getOrElse("*")
      val props = elementProps.map(e => s"[${e.toXpath}]").mkString("")
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
    val prop = new ElementProperty {
      override def toXpath(): String = s"${n + 1}"

      override def toString: String =  if (n==0) "is the first one" else "has index " + n
    }
    if (this.describedBy.isEmpty) {
      new Path(underlyingSource, xpath, elementProps = elementProps :+ prop, xpathExplanation = xpathExplanation, insideXpath = insideXpath)
    } else {
      new Path(underlyingSource, getXPath, elementProps = List(prop), xpathExplanation = this.describedBy)
    }
  }

  def that(props: ElementProperty*): Path = {
    if (describedBy.isDefined) {
      new Path(underlyingSource, xpath= getXPathWithoutInsideClause, elementProps = List(props:_*), xpathExplanation = describedBy, insideXpath = insideXpath)
    } else {
      new Path(underlyingSource, xpath = xpath, elementProps = elementProps ++ props, xpathExplanation = xpathExplanation, insideXpath = insideXpath)
    }
  }
  

  def unary_!(): Path = new Path(underlyingSource, Some(XpathUtils.DoesNotExistInEntirePage(getXPath.getOrElse(""))), xpathExplanation = Some(s"anything except (${toString()})"))

  def or(path: Path) = {
    verifyRelationBetweenElements(path)
    new Path(underlyingSource, Some(s"*[self::${getXPath.get} | self::${path.getXPath.get}]"), xpathExplanation = Some(s"${wrapIfNeeded(this)} or ${wrapIfNeeded(path)}"))
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

  private def createNewWithAdditionalProperty( prop: ElementProperty) = {
    if (describedBy.isEmpty) {
      new Path(underlyingSource, xpath, elementProps = elementProps :+ prop, insideXpath = insideXpath, xpathExplanation = xpathExplanation)
    } else {
      new Path(underlyingSource, getXPath, insideXpath = insideXpath, elementProps = List(prop), xpathExplanation = describedBy, describedBy = describedBy)
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
      insideXpath = Some(path.getXPath.get + (if (insideXpath.isDefined)  ("//" + insideXpath.get) else "")),
      xpathExplanation = Some(toString + s", inside ${wrapIfNeeded(path)}"))
  }

  private def wrapIfNeeded(path: Path): String = {
    return if ((path.toString.trim.contains(" "))) "(" + path + ")" else path.toString
  }

  def childOf(path: Path) = {
    verifyRelationBetweenElements(path)
    createWithSimpleRelation(path, "child")
  }

  def parentOf(path: Path) = {
    verifyRelationBetweenElements(path)
    createWithSimpleRelation(path, "parent")
  }

  def containing(path: Path) = ancestorOf(path)

  def containedIn(path: Path) = descendantOf(path)

  def ancestorOf(path: Path) = {
    verifyRelationBetweenElements(path)
    createWithSimpleRelation(path, "ancestor")
  }

  def descendantOf(path: Path) = {
    verifyRelationBetweenElements(path)
    createWithSimpleRelation(path, "descendant")
  }

  def afterSibling(path: Path) = {
    verifyRelationBetweenElements(path)
    createWithHumanReadableRelation(path, "following-sibling", "after the sibling")
  }

  def after(path: Path) = {
    verifyRelationBetweenElements(path)
    createWithHumanReadableRelation(path, "following", "after")
  }

  def beforeSibling(path: Path) = {
    verifyRelationBetweenElements(path)
    createWithHumanReadableRelation(path, "preceding-sibling", "before the sibling")
  }

  def before(path: Path): Path = {
    verifyRelationBetweenElements(path)
    createWithHumanReadableRelation(path, "preceding", "before")
  }

  private def verifyRelationBetweenElements(path: Path) {
    if (path.getUnderlyingSource().isDefined || !getXPath.isDefined || !path.getXPath.isDefined) throw new IllegalArgumentException()
  }

  def describedBy(txt: String) = {
    val descriptionAsOption = Some(txt)
    new Path(underlyingSource, xpath = xpath, elementProps = elementProps, xpathExplanation = xpathExplanation, describedBy = descriptionAsOption)
  }

  def toXpath() = "xpath: " + getXPath.getOrElse("")

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
     def getXpathExplanationForToString: Option[String] =
    {
      if (xpath.isDefined) {
        if (xpathExplanation.isDefined) xpathExplanation else Some("xpath: \"" + xpath.get + "\"")
      }
      else None
    }

    def getPropertiesToStringForLength1: Option[String] = {
      val firstProp = elementProps.head.toString
      val thatMaybe: String = if ((firstProp.startsWith("has ") ||firstProp.startsWith("is ") || firstProp.startsWith("not "))) "that " else ""
      Some(thatMaybe + elementProps.head)
    }

    def getPropertiesToStringForLengthLargerThan2: Option[String] = {
      val propsAsList: String = elementProps.map(e => e.toString).mkString(", ")
      if (xpathExplanation.isDefined && xpathExplanation.get.contains("with properties") || elementProps.size == 1) {
        Some("and " + propsAsList)
      }
      else {
        Some("that [" + propsAsList + "]")
      }
    }


    if (describedBy.isDefined && describedBy != xpathExplanation) {
      describedBy.get
    } else {
      val underlyingOption = if (underlyingSource.isDefined) Some(s"under reference element ${underlyingSource.get}") else None
      val xpathOption = getXpathExplanationForToString

      val propsOption: Option[String] = {
        if (elementProps.size == 1 && (!xpathOption.getOrElse("").contains(", ") || xpathOption == describedBy)) {
          getPropertiesToStringForLength1
        } else if (elementProps.size == 2 && !xpathOption.getOrElse("").contains(" ")) {
          Some(s"that ${elementProps.head}, and ${elementProps.last}")
        } else if (elementProps.size > 1 || xpathOption.getOrElse("").contains(" ") && elementProps.nonEmpty) {
          getPropertiesToStringForLengthLargerThan2
        } else None
      }

      if (xpathExplanation.isDefined && underlyingOption.isEmpty && propsOption.isEmpty) {
        xpathExplanation.get
      } else {
        List(underlyingOption, xpathOption, propsOption).flatten.mkString(", ")
      }
    }
  }


}



