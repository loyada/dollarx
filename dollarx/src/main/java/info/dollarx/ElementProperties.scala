package info.dollarx

import info.dollarx.ElementPropertiesHelper._


object ElementProperties {

  implicit def elementPropertyToPath(p: ElementProperty): Path = Path.element.that(p)


  case class hasClass(cssClass: String) extends ElementProperty {
    override def toXpath = XpathUtils.hasClass(cssClass)

    override def toString = s"""has class "$cssClass""""

  }

  def not(prop: ElementProperty): ElementProperty = {
     Not(prop)
  }

  case class isNthFromLastSibling(reverseIndex: Integer) extends ElementProperty {
      def toXpath: String = {
        return String.format("count(following-sibling::*)=%d", reverseIndex)
      }

      override def toString: String = {
        return String.format("is in place %d from the last sibling", reverseIndex)
      }
  }

  case class  isNthSibling(index: Integer) extends ElementProperty {
      def toXpath: String = {
        return String.format("count(preceding-sibling::*)=%d", index)
      }

      override def toString: String = {
        return String.format("is in place %d among its siblings", index)
      }
  }




  object has {
    import ElementPropertiesHelper.HasHelper._

    def apply(n: Int) = HasN(n)
    def child(path: Path) = hasChild(path)
    def parent(path: Path) = hasParent(path)
    def descendant(path: Path*) = hasDescendant(path:_*)
    def ancestor(path: Path) = hasAncesctor(path)
    def children = new HasChildren
    def noChildren =  HasNoChildren
    def id(theId: String) = hasId(theId)
    def cssClass(cssClass: String) = hasClass(cssClass)
    def classes(cssClasses: String*) = hasClasses(cssClasses:_*)
    def oneOfClasses(cssClasses: String*) = hasOneOfClasses(cssClasses:_*)
    def text(txt: String) = hasText(txt)
    def textContaining(txt: String) = hasTextContaining(txt)
    val someText = hasSomeText
    def aggregatedTextContaining(txt: String) = withAggregatedTextContaining(txt)
    def aggregatedText(txt: String) = withAggregatedTextEqualTo(txt)
    def attribute(key: String, value: String) = hasAttribute(key, value)

    trait HasNotProperty {
      def get: ElementProperty
    }
    object bar extends HasNotProperty {
      override def get: ElementProperty = HasNoChildren
    }

    def no(p: HasNotProperty): ElementProperty = p.get

    object not {
      object children extends HasNotProperty {
        override def get: ElementProperty = HasNoChildren
      }
      case class cssClass(cssClass: String*) extends HasNotProperty{
        override def get: ElementProperty = withoutClasses(cssClass:_*)
      }
      object text extends  HasNotProperty{
        override def get: ElementProperty = hasNoText()
      }
      case class  textEqualTo(txt: String) extends HasNotProperty {
        override def get: ElementProperty = hasNoText(txt)
      }
      case class  textContaining(txt: String) extends HasNotProperty {
        override def get: ElementProperty = Not(hasTextContaining(txt))
      }
      case class  aggregatedTextContaining(txt: String) extends HasNotProperty {
        override def get: ElementProperty = Not(withAggregatedTextContaining(txt))
      }
      case class  aggregatedText(txt: String) extends HasNotProperty {
        override def get: ElementProperty = Not(withAggregatedTextEqualTo(txt))
      }
    }




  }

  object lastSiblingOfType extends ElementProperty{
    override def toXpath = "last()"

    override def toString = "is last sibling"
  }


  object uniqueOfType extends ElementProperty {
    override def toXpath = "count(*)=1"

    override def toString = "only one of its type"
  }

  case class hasId(id: String) extends ElementProperty {
    override def toXpath = XpathUtils.hasId(id)

    override def toString = s"""has Id "$id""""
  }

  case class hasOneOfClasses(cssClasses: String*) extends ElementProperty {
    override def toXpath = XpathUtils.hasOneOfClasses(cssClasses: _*)

    override def toString = s"has at least one of the classes: [${cssClasses.mkString(", ")}]"
  }

  case class hasClasses(cssClasses: String*) extends ElementProperty {
    override def toXpath = XpathUtils.hasClasses(cssClasses: _*)

    override def toString = s"has classes [${cssClasses.mkString(", ")}]"
  }

  case class withoutClasses(cssClasses: String*) extends ElementProperty {
    override def toXpath =  XpathUtils.DoesNotExist(XpathUtils.hasOneOfClasses(cssClasses: _*))

    override def toString = {
      if (cssClasses.size>1) {
        s"has non of the classes: [${cssClasses.mkString(", ")}]"
      }
      else s"does not have the class ${cssClasses.mkString(", ")}"

    }
  }


  case class raw(rawXpathProps: String, explanation: String) extends ElementProperty {
    override def toXpath(): String = rawXpathProps

    override def toString() = explanation
  }

  case class hasText(txt: String) extends ElementProperty {
    override def toXpath() = XpathUtils.textEquals(txt)

    override def toString() = s"""has the text "${txt}""""
  }

  case class hasNoText(txt: String = "") extends ElementProperty {
    override def toXpath() = {
      val hasItProperty = if (txt=="") hasSomeText else hasText(txt)
      val hasNoProperty = not(hasItProperty)
      hasNoProperty.toXpath
    }

    override def toString() = if (txt=="") "has no text" else s"""has no text equal to "${txt}""""
  }

  case class hasTextContaining(txt: String) extends ElementProperty {
    override def toXpath() = XpathUtils.textContains(txt)

    override def toString() = s"""has text containing "${txt}""""

  }

  case class withAggregatedTextEqualTo(txt: String) extends ElementProperty {
    override def toXpath() = XpathUtils.aggregatedTextEquals(txt)

    override def toString() = s"""with aggregated text "$txt""""

  }

  case class withAggregatedTextContaining(txt: String) extends ElementProperty {
    override def toXpath() = XpathUtils.aggregatedTextContains(txt)

    override def toString() = s"""with aggregated text containing "$txt""""
  }

  object hasSomeText extends ElementProperty {
    override def toXpath() = XpathUtils.hasSomeText

    override def toString() = "has some text"

  }

  object isHidden extends ElementProperty with IsProperty{
    override def toXpath() = XpathUtils.isHidden

    override def toString() = "is hidden"
  }

  case class isChildOf(path: Path) extends ElementProperty with relationBetweenElement {
    override def toXpath() = getRelationXpath("parent")

    override def toString() = {
      "is child of: " + path
    }

  }

  def contains(paths: Path*) = hasDescendant(paths:_*)

  case class hasDescendant(paths: Path*) extends ElementProperty with relationBetweenMultiElement {
    protected val relation = "descendant"
    override def toString() = asString("has descendant")
  }

  case class hasAncesctor(path: Path) extends ElementProperty with relationBetweenElement with IsProperty{
    override def toXpath() = getRelationXpath("ancestor")

    override def toString = {
      "has ancestor: " + rValueToString(path)
    }

  }

  case class hasParent(path: Path) extends ElementProperty with relationBetweenElement {
    override def toXpath() = getRelationXpath("parent")
    override def toString() = {
      "has parent: " + rValueToString(path)
    }
  }

  case class hasChild(paths: Path*) extends ElementProperty with relationBetweenMultiElement {
    protected val relation = "child"
    override def toString() = asString("has " + (if (paths.size==1) "child" else "children"))
    override protected def plural(relation: String)  = relation

  }

  case class hasAttribute(attribute: String, value: String) extends ElementProperty {
     override def toXpath: String = {
         XpathUtils.hasAttribute(attribute, value)
      }

      override def toString: String = {
         String.format("has %s: \"%s\"", attribute, value)
      }
  }


  object is {

    import ElementPropertiesHelper.IsHelpers._

    def not(e: IsProperty) = Not(e)
    def after(nPath: NPath) = IsAfterProperty(nPath)

    def before(nPath: NPath) = IsBeforeProperty(nPath)

    def afterSibling(path: Path*) = IsAfterSiblingProperty(path: _*)

    def afterSibling(nPath: NPath) = IsAfterSiblingProperty(nPath)

    def inside(path: Path) = hasAncesctor(path)

    def containedIn(path: Path) = inside(path)

    def childOf(path: Path) = isChildOf(path)

    def descendantOf(path: Path) = inside(path)

    def ancestorOf(paths: Path*) = hasDescendant(paths: _*)

    def parentOf(paths: Path*) = hasChild(paths: _*)

    def before(paths: Path*) = IsBeforeProperty(paths: _*)

    def after(paths: Path*) = IsAfterProperty(paths: _*)

    def beforeSibling(paths: Path*) = IsBeforeSiblingProperty(paths: _*)

    def beforeSibling(nPath: NPath) = IsBeforeSiblingProperty(nPath)

    def nthFromLastSibling(n: Int) = isNthFromLastSibling(n)

    def nthSibling(n: Int) = isNthSibling(n)

    val lastSibling = lastSiblingOfType
    val firstSibling = withIndex(0)

    def withIndex(index: Int) = IsWithIndex(index)

    val hidden = isHidden

    val onlyChild: ElementProperty = new ElementProperty {
      override def toXpath: String = "count(preceding-sibling::*)=0 and count(following-sibling::*)=0"

      override def toString: String = "is only child"
    }

    def withIndexInRange(first: Int, last: Int): ElementProperty = {
      IswithIndexInRange(first, last)
    }
  }



  implicit def intToNPathBuilder(n: Int): NPathBuilder = NPathBuilder(n)

  private def rValueToString(path: Path): String = {
     if ((path.toString.trim.contains(" "))) "(" + path + ")" else path.toString
  }

  case class isAfter(paths: Path*) extends ElementProperty with relationBetweenMultiElement {
    protected val relation = "preceding"
    override def toString() = asString("is after")
    override protected def plural(relation: String)  = relation
  }

  case class isAfterSibling(paths: Path*) extends ElementProperty with relationBetweenMultiElement {
    protected val relation = "preceding-sibling"
    override def toString() = asString("is after sibling")
  }

  case class isBefore(paths: Path*) extends ElementProperty with relationBetweenMultiElement {
    protected val relation = "following"
    override def toString() = asString("is before")
  }

  case class isBeforeSibling(paths: Path*) extends ElementProperty with relationBetweenMultiElement {
    protected val relation = "following-sibling"
    override def toString() = asString("is before sibling")
  }

}
