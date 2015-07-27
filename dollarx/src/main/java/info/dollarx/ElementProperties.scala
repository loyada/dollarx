package info.dollarx

object ElementProperties {

  implicit def elementPropertyToPath(p: ElementProperty): Path = Path.element.that(p)

  case class Or(p1: ElementProperty, p2: ElementProperty) extends ElementProperty {
    override def toString() = s"($p1 or $p2)"

    override def toXpath() = p1.toXpath + " or " + p2.toXpath
    
    override def or(p: ElementProperty) = Or(this, p)
    //we commented the line below, since it will transform logic like : [(A or B) and C]  to [A or (B and C)], which is bad
    //override def and(p: ElementProperties) = Or(p1, And(p2, p))
  }

  case class And(p1: ElementProperty, p2: ElementProperty) extends ElementProperty {
    override def toXpath() = {
      "(" + p1.toXpath + " and " + p2.toXpath + ")"
    }

    override def toString() = {
      s"($p1 and $p2)"
    }
  }

  case class Not(p: ElementProperty) extends ElementProperty {
    override def toString() = "not(" + p.toString + ")"

    override def toXpath = "not(" + p.toXpath + ")"
  }

  case class hasClass(cssClass: String) extends ElementProperty {
    override def toXpath = XpathUtils.hasClass(cssClass)

    override def toString = s"""has class "$cssClass""""

  }

  object has {
    def apply(n: Int) = HasN(n)
    def children = new HasChildren
    def noChildren =  HasNoChildren
    def id(theId: String) = hasId(theId)
    def cssClass(cssClass: String) = hasClass(cssClass)
    def classes(cssClasses: String*) = hasClasses(cssClasses:_*)
    def oneOfClasses(cssClasses: String*) = hasOneOfClasses(cssClasses:_*)
    def text(txt: String) = hasText(txt)
    def textContaining(txt: String) = hasTextContaining(txt)
    def someText() = hasSomeText
    def aggregatedTextContaining(txt: String) = withAggregatedTextContaining(txt)
    def aggregatedText(txt: String) = withAggregatedTextEqualTo(txt)

    object no {
      def children =  HasNoChildren
      def cssClass(cssClass: String*) = withoutClasses(cssClass:_*)
      def text(txt: String) = Not(hasText(txt))
      def textContaining(txt: String) = Not(hasTextContaining(txt))
      def aggregatedTextContaining(txt: String) = Not(withAggregatedTextContaining(txt))
      def aggregatedText(txt: String) = Not(withAggregatedTextEqualTo(txt))
    }

    private val countXpath : (String => String) = {(relation: String) => s"count($relation::* )" }
    private val countChildrenXpath = "count(./*)"
    private val countDescendantsXpath = countXpath("descendant")
    private val countSiblingsXpath = countXpath("sibling")

    class HasChildren(n: Option[Int] = None) extends ElementProperty {
      override def toXpath = if (n.isEmpty) s"$countChildrenXpath > 0" else s"$countChildrenXpath=${n.get}"

      override def toString = {
        val number = if (n.isDefined) (" " + n.get) else ""
        s"has$number children"
      }
    }

    class HasDescendants(n: Option[Int] = None) extends ElementProperty {
      override def toXpath = if (n.isEmpty) s"$countChildrenXpath > 0" else s"$countChildrenXpath > ${n.get}"

      override def toString = {
        val number = if (n.isDefined) (" " + n.get) else ""
        s"has$number children"
      }
    }

    object HasNoChildren extends ElementProperty {
      override def toXpath = s"$countChildrenXpath = 0"

      override def toString = "has no children"
    }

    case class HasN(n: Int) {
      def children = new HasChildren(Some(n))
   //   def siblings = new HasSiblings(Some(n))
      def descendants = new HasDescendants(Some(n))

    }

  }

  object lastSiblingOfType extends ElementProperty{
    override def toXpath = "last()"

    override def toString = "last of its type"  }


  object uniqueOfType extends ElementProperty {
    override def toXpath = "count(*)=1"

    override def toString = "only one of its type"
  }

  case class hasId(id: String) extends ElementProperty {
    override def toXpath = XpathUtils.hasId(id)

    override def toString = s"has Id $id"
  }

  case class hasOneOfClasses(cssClasses: String*) extends ElementProperty {
    override def toXpath = XpathUtils.hasOneOfClasses(cssClasses: _*)

    override def toString = s"has at least one of classes: [${cssClasses.mkString(", ")}]"
  }

  case class hasClasses(cssClasses: String*) extends ElementProperty {
    override def toXpath = XpathUtils.hasClasses(cssClasses: _*)

    override def toString = s"has classes: [${cssClasses.mkString(", ")}]"
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

  case class withIndex(n: Int) extends ElementProperty {
    override def toXpath() = s"position()=${n + 1}"

    override def toString() = s"with index $n"
  }


  case class raw(rawXpathProps: String, explanation: String) extends ElementProperty {
    override def toXpath(): String = rawXpathProps

    override def toString() = explanation
  }

  case class hasText(txt: String) extends ElementProperty {
    override def toXpath() = XpathUtils.textEquals(txt)

    override def toString() = s"""with the text "${txt}""""


  }

  case class hasTextContaining(txt: String) extends ElementProperty {
    override def toXpath() = XpathUtils.textContains(txt)

    override def toString() = s"""with text containing "${txt}""""

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

  object isHidden extends ElementProperty {
    override def toXpath() = XpathUtils.isHidden

    override def toString() = "hidden"
  }

  case class childOf(path: Path) extends ElementProperty with relationBetweenElement {
    override def toXpath() = getRelationXpath("parent")

    override def toString() = {
      "child of: " + path
    }

  }

  case class contains(webElements: Path*) extends ElementProperty {
    override def toXpath() = {
      val xpaths = webElements.map(path => {
        hasDescendant(path)
      })
      "(" + xpaths.mkString(" and ") + ")"
    }

    override def toString() = {
      "has descendants: " + webElements.mkString(", ")
    }
  }

  case class hasDescendant(path: Path) extends ElementProperty with relationBetweenElement {
    override def toXpath() = getRelationXpath("descendant")

    override def toString() = {
      "has descendant: " + rValueToString(path)
    }
  }

  case class hasAncesctor(path: Path) extends ElementProperty with relationBetweenElement {
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

  case class hasChild(path: Path) extends ElementProperty with relationBetweenElement {
    override def toXpath() = getRelationXpath("child")
    override def toString() = {
      "has child: " + rValueToString(path)
    }
  }


  object is {

    case class Sibling(path: Path)


    class IsAfter {

      case class isAfterProp(path: Path) extends ElementProperty with relationBetweenElement {
        override def toXpath() = getRelationXpath("preceding")

        override def toString() = {
          "is after: " + rValueToString(path)
        }
      }

      def sibling(path: Path) = isAfterProp(path)
    }

    private case class IsAfterN(n: Int) {
      def apply() = IsAfterN(0)

      case class isAfterProp(path: Path) extends ElementProperty with relationBetweenElement {
        override def toXpath() = getRelationXpath("preceding")

        override def toString() = {
          "is after: " + rValueToString(path)
        }
      }

      def siblings(path: Path) = isAfterProp(path)
    }

    def afterSibling(path: Path) = new IsAfter
    def inside(path: Path) = hasAncesctor(path)

    //NElement => 5 div
    //NElement => div
    //NElement => sibling div
    //NElement => 5 sibling div

    //  def after(n:Int) =  IsAfterN(n)
    object after {
      def apply(path: Path) = isAfter(path)

  //    def apply(n: Int): path => ElementProperties = ((path: path) => isAfter(n, path))

      private case class isAfterNBuilder(n: Int)

    }

    object isAfter {

      case class isAfterProp(path: Path) extends ElementProperty with relationBetweenElement {
        override def toXpath() = getRelationXpath("preceding")

        override def toString() = {
          "is after: " + path
        }
      }

      def apply(path: Path) = isAfterProp(path)
    }

  }

  private def rValueToString(path: Path): String = {
     if ((path.toString.trim.contains(" "))) "(" + path + ")" else path.toString
  }

  case class isAfter(path: Path) extends ElementProperty with relationBetweenElement {
    override def toXpath() = getRelationXpath("preceding")
    override def toString() = {
      "is after: " + rValueToString(path)
    }
  }

  case class isAfterSibling(path: Path) extends ElementProperty with relationBetweenElement {
    override def toXpath() = getRelationXpath("preceding-sibling")
    override def toString() = {
      "is after sibling: " + rValueToString(path)
    }
  }

  case class isBefore(path: Path) extends ElementProperty with relationBetweenElement {
    override def toXpath() = getRelationXpath("following")
    override def toString() = {
      "is before: " + rValueToString(path)
    }
  }

  case class isBeforeSibling(path: Path) extends ElementProperty with relationBetweenElement {
    override def toXpath() = getRelationXpath("preceding-sibling")
    override def toString() = {
      "is before sibling: " + rValueToString(path)
    }
  }

}

trait relationBetweenElement {
  val path: Path

  protected def getRelationXpath(relation: String) = {
    if (path.getUnderlyingSource().isDefined || path.getXPath.isEmpty) throw new IllegalArgumentException("must use a pure xpath path")
    s"$relation::" + path.getXPath.get
  }
}

