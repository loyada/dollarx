package com.github.loyada.dollarx

import com.github.loyada.dollarx.util.XpathUtils
import org.openqa.selenium.WebElement
import com.github.loyada.dollarx.ElementPropertiesHelper.{hasHierarchy, transformXpathToCorrectAxis}

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
  val tr = new Path(xpath = Some("tr"), xpathExplanation = Some("table row"))
  val td = new Path(xpath = Some("td"), xpathExplanation = Some("table cell"))
  val th = new Path(xpath = Some("th"), xpathExplanation = Some("table header cell"))
  val table = customElement("table")
  val select = new Path(xpath = Some("select"), xpathExplanation = Some("selection menu"))
  val option = customElement("option")
  val paragraph = new Path(xpath = Some("p"), xpathExplanation = Some("paragraph"))

  def customElement(el: String): Path = new Path(xpath = Some(el), xpathExplanation = Some(el))

  def apply(path: String): Path = {
    new Path(xpath = Some(path))
  }

  implicit def webElementToPath(we: WebElement): Path = {
    new Path(Some(we))
  }

  object first {
    /**
      *
      * @param path
      * @return The first occurrence of path in the document
      */
    def occurrenceOf(path: Path): Path = path(0)
  }

  object last {
    /**
      *
      * @param path
      * @return The last occurrence of path in the document
      */
    def occurrenceOf(path: Path): Path = path(-1)
  }

  case class childNumber(n: Int) {
    /**
      *
      * @param path
      * @return all the elements that are child number n of type path. This is different than path(n), which is global.
      */
    def ofType(path: Path): Path = {
      val newXpath = path.getXPath.get + s"[${n}]"
      val alternateXpath = path.getAlternateXPath.get + s"[${n}]"
      new Path(path.underlyingSource, xpath = Some(newXpath), elementProps = List(),
        xpathExplanation = Some(s"child number $n of type($path)"), alternateXpath = Some(alternateXpath))
    }
  }

}


class Path(val underlyingSource: Option[WebElement] = None, val xpath: Option[String] = None, val insideXpath: Option[String] = None,
           val elementProps: List[ElementProperty] = Nil, val xpathExplanation: Option[String] = None, val describedBy: Option[String] = None,
           val alternateXpath: Option[String] = None) {


  def getXPath: Option[String] = {
    if (xpath.isEmpty && elementProps.isEmpty && insideXpath.isEmpty) {
      None
    } else {
      val processedXpath = (if (insideXpath.isDefined) (insideXpath.get + "//") else "") + xpath.getOrElse("*")
      val props = elementProps.map(e => s"[${e.toXpath}]").mkString("")
      Some(processedXpath + props)
    }
  }

  def getAlternateXPath: Option[String] = {
    if (xpath.isEmpty && elementProps.isEmpty && insideXpath.isEmpty) {
      None
    } else {
      val props = elementProps.map(e => s"[${e.toXpath}]").mkString("")
      Some(alternateXpath.getOrElse(xpath.getOrElse("*")) + props)
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

  /**
    *
    * @param n
    * @return always a single element, since this is simply nth element of type path in the document.
    */
  def apply(n: Int) = {
    val prefix = if (n == 0) "the first occurrence of " else {
      if (n == -1) "the last occurence of " else s"occurrence number ${n + 1} of "
    }
    val pathString = this.toString()
    val wrapped = if (pathString.contains(" ")) s"($pathString)" else pathString
    val index: String = if (n== -1) "last()" else s"${n+1}"
    val xpathPrefix = if (getXPath.get.startsWith("(")) "(" else "(//"
    new Path(underlyingSource, Some(s"$xpathPrefix${getXPath.get})[$index]"), xpathExplanation = Some(prefix + wrapped),
      alternateXpath = Some(s"$xpathPrefix${getAlternateXPath.get})[$index]"))
  }

  def that(props: ElementProperty*): Path = {
    if (describedBy.isDefined) {
      new Path(underlyingSource, xpath = getXPathWithoutInsideClause, elementProps = List(props: _*), xpathExplanation = describedBy, insideXpath = insideXpath,
        alternateXpath = alternateXpath)
    } else {
      new Path(underlyingSource, xpath = xpath, elementProps = elementProps ++ props, xpathExplanation = xpathExplanation, insideXpath = insideXpath, alternateXpath = alternateXpath)
    }
  }

  def and(props: ElementProperty*): Path = that(props: _*)

  def unary_!(): Path = new Path(underlyingSource,
    Some(XpathUtils.DoesNotExistInEntirePage(getXPath.getOrElse(""))),
    xpathExplanation = Some(s"anything except (${toString()})"),
    alternateXpath = Some(XpathUtils.DoesNotExistInEntirePage(getAlternateXPath.getOrElse(""))) )

  def or(path: Path) = {
    verifyRelationBetweenElements(path)
    new Path(underlyingSource, Some(s"*[(self::${transformXpathToCorrectAxis(this).get}) | (self::${transformXpathToCorrectAxis(path).get})]"),
      alternateXpath =  Some(s"*[(self::${getAlternateXPath.get}) | (self::${path.getAlternateXPath.get})]"),
      xpathExplanation = Some(s"${wrapIfNeeded(this)} or ${wrapIfNeeded(path)}"))
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

  private def createNewWithAdditionalProperty(prop: ElementProperty) = {
    if (describedBy.isEmpty) {
      new Path(underlyingSource, xpath, elementProps = elementProps :+ prop, insideXpath = insideXpath,
        xpathExplanation = xpathExplanation, alternateXpath = alternateXpath)
    } else {
      new Path(underlyingSource, getXPath, insideXpath = insideXpath, elementProps = List(prop), xpathExplanation = describedBy, describedBy = describedBy,
        alternateXpath = alternateXpath)
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
    val newXPath = getXPathWithoutInsideClause.getOrElse("")
    val (correctedXpathForIndex, correctInsidePath) = if (newXPath startsWith ("(")) {
      ((newXPath + s"[${ElementProperties.is.inside(path).toXpath}]"), None)
    } else (newXPath, Some(path.getXPath.get + (if (insideXpath.isDefined) ("//" + insideXpath.get) else "")))
    new Path(path.getUnderlyingSource(),
      xpath = Some(correctedXpathForIndex),
      insideXpath = correctInsidePath,
      alternateXpath = this.that(ElementProperties.is.descendantOf(path)).getAlternateXPath,
      xpathExplanation = Some(toString + s", inside ${wrapIfNeeded(path)}"))

  }

  def insideTopLevel: Path = {
    if (getXPath.isEmpty) throw new IllegalArgumentException("must have a non-empty xpath")
    new Path(
      getUnderlyingSource(),
      xpath = Some(XpathUtils.insideTopLevel(getXPath.get)),
      describedBy = Some(toString()),
      alternateXpath = Some(XpathUtils.insideTopLevel(getAlternateXPath.get)))
  }

  private def wrapIfNeeded(path: Path): String = {
    if ((path.toString.trim.contains(" "))) "(" + path + ")" else path.toString
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
    new Path(underlyingSource, xpath = xpath, elementProps = elementProps, insideXpath = insideXpath,
      xpathExplanation = xpathExplanation,
      alternateXpath = alternateXpath,
      describedBy = descriptionAsOption)
  }

  def toXpath() = "xpath: " + getXPath.getOrElse("")

  private def createWithSimpleRelation(path: Path, relation: String): Path = {
    verifyRelationBetweenElements(path)
    val myXpath: String = getXPath.get
    val isInside: Boolean = insideXpath.isDefined
    val processedXpath: String = if (isInside) s"*[ancestor::${insideXpath.get} and self::${xpath.getOrElse("*")}]" else myXpath
    val newAlternateXpath = getAlternateXPath.get + s"[${ElementPropertiesHelper.oppositeRelation(relation)}::${path.getAlternateXPath.get}]"
    val useAlternateXpath = hasHierarchy(processedXpath)
    val newXpath = if (useAlternateXpath) newAlternateXpath else path.getXPath.get + "/" + relation + "::" + processedXpath
    new Path(underlyingSource = underlyingSource,
      xpath = Some(newXpath),
      alternateXpath = Some(newAlternateXpath),
      xpathExplanation = Some(toString + ", " +  relation + " of " + path.toString))
  }

  private def createWithHumanReadableRelation(path: Path, xpathRelation: String, humanReadableRelation: String): Path = {
    verifyRelationBetweenElements(path)
    val myXpath: String = getXPath.get
    val isInside: Boolean = insideXpath.isDefined
    val processedXpath: String = if (isInside) s"${getXPathWithoutInsideClause.get}[ancestor::${insideXpath.get}]" else myXpath
    val newAlternateXpath = getAlternateXPath.get + s"[${ElementPropertiesHelper.oppositeRelation(xpathRelation)}::${path.getAlternateXPath.get}]"
    val useAlternateXpath = hasHierarchy(processedXpath)
    val newXpath = if (useAlternateXpath) newAlternateXpath else (path.getXPath.get + "/" + xpathRelation + "::" + processedXpath)
    new Path(underlyingSource = underlyingSource,
      xpath = Some(newXpath),
      alternateXpath = Some(newAlternateXpath),
      xpathExplanation = Some(toString + ", " + humanReadableRelation + " " + wrapIfNeeded(path)))
  }

  override def toString() = {
    def getXpathExplanationForToString: Option[String] = {
      if (xpath.isDefined) {
        if (xpathExplanation.isDefined) xpathExplanation else Some("xpath: \"" + xpath.get + "\"")
      }
      else None
    }

    def getPropertiesToStringForLength1: Option[String] = {
      val firstProp = elementProps.head.toString
      val thatMaybe: String = if ((firstProp.startsWith("has ") || firstProp.startsWith("is ") || firstProp.startsWith("not "))) "that " else ""
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



