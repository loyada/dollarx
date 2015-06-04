package dollarx

import dollarx.ElementProperties.{last, index}


object ElementProperties {

  implicit def elementPropertiesToWebEl(p: ElementProperties) = WebEl.element.withProperties(p)

  case class Or(p1: ElementProperties, p2: ElementProperties) extends ElementProperties {
    override def toString() = s"($p1 or $p2)"

    override def toXpath() = p1.toXpath + " or " + p2.toXpath
    
    override def or(p: ElementProperties) = Or(this, p)
    //we commented the line below, since it will transform logic like : [(A or B) and C]  to [A or (B and C)], which is bad
    //override def and(p: ElementProperties) = Or(p1, And(p2, p))
  }

  case class And(p1: ElementProperties, p2: ElementProperties) extends ElementProperties {
    override def toXpath() = {
      "(" + p1.toXpath + " and " + p2.toXpath + ")"
    }

    override def toString() = {
      s"($p1 and $p2)"
    }
  }

  case class Not(p: ElementProperties) extends ElementProperties {
    override def toString() = "not(" + p.toString + ")"

    override def toXpath() = "not(" + p.toXpath + ")"
  }

  case class hasClass(cssClass: String) extends ElementProperties {
    override def toXpath = XpathUtils.hasClass(cssClass)

    override def toString = s"""has class "$cssClass""""

  }

  object has {
    def apply(n: Int) = HasN(n)
    def children = new HasChildren
    def noChildren = new HasNoChildren
    def id(theId: String) = hasId(theId)
    def cssClass(cssClass: String) = hasClass(cssClass)
    def classes(cssClasses: String*) = hasClasses(cssClasses:_*)
    def oneOfClasses(cssClasses: String*) = hasOneOfClasses(cssClasses:_*)
    def text(txt: String) = hasText(txt)
    def textContaining(txt: String) = hasTextContaining(txt)
    def someTxt() = withSomeText


    object no {
      def children = new HasNoChildren
      def cssClass(cssClass: String*) = withoutClasses(cssClass:_*)
      def text(txt: String) = Not(hasText(txt))
      def textContaining(txt: String) = Not(hasTextContaining(txt))
    }

    private val countChildrenXpath = "count(child::node() )"

    class HasChildren(n: Option[Int] = None) extends ElementProperties {
      override def toXpath = if (n.isEmpty) s"$countChildrenXpath > 0" else s"$countChildrenXpath > ${n.get}"

      override def toString = {
        val number = if (n.isDefined) (" " + n.get) else ""
        s"has$number children"
      }
    }

    class HasNoChildren extends ElementProperties {
      override def toXpath = s"$countChildrenXpath = 0"

      override def toString = "has no children"
    }

    case class HasN(n: Int) {
      def children = new HasChildren(Some(n))
    }

  }

  object lastOfType extends ElementProperties{
    override def toXpath = "last()"

    override def toString = "last of its type"  }


  object uniqueOfType extends ElementProperties{
    override def toXpath = "count(*)=1"

    override def toString = "only one of its type"
  }

  case class hasId(id: String) extends ElementProperties {
    override def toXpath = XpathUtils.hasId(id)

    override def toString = s"has Id $id"
  }

  case class hasOneOfClasses(cssClasses: String*) extends ElementProperties {
    override def toXpath = XpathUtils.hasOneOfClasses(cssClasses: _*)

    override def toString = s"has at least one of classes: [${cssClasses.mkString(", ")}]"
  }

  case class hasClasses(cssClasses: String*) extends ElementProperties {
    override def toXpath = XpathUtils.hasClasses(cssClasses: _*)

    override def toString = s"has classes: [${cssClasses.mkString(", ")}]"
  }

  case class withoutClasses(cssClasses: String*) extends ElementProperties {
    override def toXpath =  XpathUtils.DoesNotExist(XpathUtils.hasOneOfClasses(cssClasses: _*))

    override def toString = {
      if (cssClasses.size>1) {
        s"has non of the classes: [${cssClasses.mkString(", ")}]"
      }
      else s"does not have the class ${cssClasses.mkString(", ")}"

    }
  }


  case class index(n: Int, p: ElementProperties = null) extends ElementProperties {
    override def toXpath() = (if (p != null) "(" + p.toXpath + ")" else "") + s"[position()=${n + 1}]"

    override def toString() = (if (p != null) (p.toString + " ") else "") + s"with index $n"
  }

  case class last(p: ElementProperties = null) extends ElementProperties {
    override def toXpath() = (if (p != null) "(" + p.toXpath + ")" else "") + "[last()]"

    override def toString() = (if (p != null) p.toString + " " else " ") + "(is last one)"

  }

  case class raw(rawXpathProps: String) extends ElementProperties {
    override def toXpath(): String = rawXpathProps

    override def toString() = s""""$rawXpathProps""""
  }

  case class hasText(txt: String) extends ElementProperties {
    override def toXpath() = XpathUtils.textEquals(txt)

    override def toString() = s"""with text equal to "${txt}""""


  }

  case class hasTextContaining(txt: String) extends ElementProperties {
    override def toXpath() = XpathUtils.textContains(txt)

    override def toString() = s"""with text containing "${txt}""""

  }

  case class withAggregatedTextEqualTo(txt: String) extends ElementProperties {
    override def toXpath() = XpathUtils.aggregatedTextEquals(txt)

    override def toString() = s"""with aggregated text "$txt""""

  }

  case class withAggregatedTextContaining(txt: String) extends ElementProperties {
    override def toXpath() = XpathUtils.aggregatedTextContains(txt)

    override def toString() = s"""with aggregated text containing "$txt""""
  }

  object withSomeText extends ElementProperties {
    override def toXpath() = XpathUtils.hasSomeText

    override def toString() = "with some text"

  }

  object isHidden extends ElementProperties {
    override def toXpath() = XpathUtils.hasSomeText

    override def toString() = "hidden"
  }

  case class childOf(webEl: WebEl) extends ElementProperties with relationBetweenElement {
    override def toXpath() = getRelationXpath("parent")

    override def toString() = {
      "child of: " + webEl
    }

  }

  case class contains(webElements: WebEl*) extends ElementProperties {
    override def toXpath() = {
      val xpaths = webElements.map(webEl => {
        hasDescendant(webEl)
      })
      "(" + xpaths.mkString(" and ") + ")"
    }

    override def toString() = {
      "has descendants: " + webElements.mkString(", ")
    }
  }

  case class hasDescendant(webEl: WebEl) extends ElementProperties with relationBetweenElement {
    override def toXpath() = getRelationXpath("descendant")

    override def toString() = {
      "has descendant: " + webEl
    }
  }

  case class hasAncesctor(webEl: WebEl) extends ElementProperties with relationBetweenElement {
    override def toXpath() = getRelationXpath("ancestor")

    override def toString() = {
      "has ancestor: " + webEl
    }

  }

  case class hasParent(webEl: WebEl) extends ElementProperties with relationBetweenElement {
    override def toXpath() = getRelationXpath("parent")
    override def toString() = {
      "has parent: " + webEl
    }
  }

  case class hasChild(webEl: WebEl) extends ElementProperties with relationBetweenElement {
    override def toXpath() = getRelationXpath("child")
    override def toString() = {
      "has child: " + webEl
    }
  }


  object is {

    case class Sibling(webEl: WebEl)


    class IsAfter {

      case class isAfterProp(webEl: WebEl) extends ElementProperties with relationBetweenElement {
        override def toXpath() = getRelationXpath("preceding")

        override def toString() = {
          "is after: " + webEl
        }
      }

      def sibling(webEl: WebEl) = isAfterProp(webEl)
    }

    private case class IsAfterN(n: Int) {
      def apply() = IsAfterN(0)

      case class isAfterProp(webEl: WebEl) extends ElementProperties with relationBetweenElement {
        override def toXpath() = getRelationXpath("preceding")

        override def toString() = {
          "is after: " + webEl
        }
      }

      def siblings(webEl: WebEl) = isAfterProp(webEl)
    }

    def afterSibling(webEl: WebEl) = new IsAfter

    //NElement => 5 div
    //NElement => div
    //NElement => sibling div
    //NElement => 5 sibling div

    //  def after(n:Int) =  IsAfterN(n)
    object after {
      def apply(webEl: WebEl) = isAfter(webEl)

      def apply(n: Int): WebEl => ElementProperties = ((webEl: WebEl) => isAfter(webEl))

      private case class isAfterNBuilder(n: Int)

    }

    object isAfter {

      case class isAfterProp(webEl: WebEl) extends ElementProperties with relationBetweenElement {
        override def toXpath() = getRelationXpath("preceding")

        override def toString() = {
          "is after: " + webEl
        }
      }

      def apply(webEl: WebEl) = isAfterProp(webEl)
    }

  }

  case class isAfter(webEl: WebEl) extends ElementProperties with relationBetweenElement {
    override def toXpath() = getRelationXpath("preceding")
    override def toString() = {
      "is after: " + webEl
    }
  }

  case class isAfterSibling(webEl: WebEl) extends ElementProperties with relationBetweenElement {
    override def toXpath() = getRelationXpath("preceding-sibling")
    override def toString() = {
      "is after sibling: " + webEl
    }
  }

  case class isBefore(webEl: WebEl) extends ElementProperties with relationBetweenElement {
    override def toXpath() = getRelationXpath("following")
    override def toString() = {
      "is before: " + webEl
    }
  }

  case class isBeforeSibling(webEl: WebEl) extends ElementProperties with relationBetweenElement {
    override def toXpath() = getRelationXpath("preceding-sibling")
    override def toString() = {
      "is before sibling: " + webEl
    }
  }

}

trait relationBetweenElement {
  val webEl: WebEl

  protected def getRelationXpath(relation: String) = {
    if (webEl.getUnderlyingSource().isDefined || webEl.getXPath().isEmpty) throw new IllegalArgumentException("must use a pure xpath WebEl")
    s"$relation::" + webEl.getXPath().get
  }
}

trait ElementProperties {


  def toXpath(): String

  def or(p: ElementProperties): ElementProperties = ElementProperties.Or(this, p)

  def and(p: ElementProperties): ElementProperties = {
    ElementProperties.And(this, p)
  }

  def andNot(p: ElementProperties): ElementProperties = {
    and(ElementProperties.Not(p))
  }

  def withIndex(n: Int): ElementProperties = {
    index(n, this)
  }


  def andIsTheLastOne() = {
    last(this)
  }

}

