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
    override def toString() = "not (" + p.toString + ")"

    override def toXpath = "not(" + p.toXpath + ")"
  }

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
    def apply(n: Int) = HasN(n)
    def child(path: Path) = hasChild(path)
    def parent(path: Path) = hasParent(path)
    def ancestor(path: Path) = hasAncesctor(path)
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
    def attribute(key: String, value: String) = hasAttribute(key, value)

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
        val number = if (n.isDefined) (" " + n.get) else " some"
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

  case class IsWithIndex(n: Int) extends ElementProperty {
    override def toXpath() = s"position()=${n + 1}"

    override def toString() = s"with index $n"
  }


  case class raw(rawXpathProps: String, explanation: String) extends ElementProperty {
    override def toXpath(): String = rawXpathProps

    override def toString() = explanation
  }

  case class hasText(txt: String) extends ElementProperty {
    override def toXpath() = XpathUtils.textEquals(txt)

    override def toString() = s"""has the text "${txt}""""


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

  object isHidden extends ElementProperty {
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

  case class hasChild(paths: Path*) extends ElementProperty with relationBetweenMultiElement {
    protected val relation = "child"
    override def toString() = asString("has " + (if (paths.size==1) "child" else "children"))
    override protected def s(relation: String)  = relation

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

    def afterSibling(path: Path*) = isAfterSibling(path:_*)
    def inside(path: Path) = hasAncesctor(path)
    def containedIn(path: Path) = inside(path)
    def childOf(path: Path) = isChildOf(path)
    def descendantOf(path: Path) = inside(path)
    def ancestorOf(paths: Path*) = hasDescendant(paths:_*)
    def parentOf(paths: Path*) = hasChild(paths:_*)
    def before(paths: Path*) = isBefore(paths:_*)
    def beforeSibling(paths: Path*) = isBeforeSibling(paths:_*)
    def nthFromLastSibling(n: Int) = isNthFromLastSibling(n)
    def nthSibling(n: Int) = isNthSibling(n)


    val lastSibling = lastSiblingOfType
    def withIndex(index: Int) = IsWithIndex(index)
    val hidden = isHidden

    val onlyChild: ElementProperty = new ElementProperty{
      override def toXpath: String = {
         "count(preceding-sibling::*)=0 and count(following-sibling::*)=0"
      }

      override def toString: String = {
         "is only child"
      }
    }

    def withIndexInRange(first: Int, last: Int): ElementProperty = {
      IswithIndexInRange(first, last)
    }
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

  case class isAfter(paths: Path*) extends ElementProperty with relationBetweenMultiElement {
    protected val relation = "preceding"
    override def toString() = asString("is after")
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
  
  case class  IswithIndexInRange(first: Int, last: Int) extends ElementProperty {
    override def toXpath: String = {
      s"position()>=${first+1} and position()<=${last+1}"
    }

    override def toString: String = {
      s"with index from $first to $last"
    }
  }

}

trait relationBetweenElement {
  protected val path: Path

  protected def getRelationXpath(relation: String) = {
    if (path.getUnderlyingSource().isDefined || path.getXPath.isEmpty) throw new IllegalArgumentException("must use a pure xpath path")
    s"$relation::" + path.getXPath.get
  }
}

trait relationBetweenMultiElement{
  protected val relation: String
  protected val paths: Seq[Path]

  def toXpath() = {
    getRelationXpath(relation)
  }

  protected def asString(prefix: String) = {
    val asList = List(paths:_*).map(path => rValueToString(path)).mkString(", ")
    s"${s(prefix)}: " + (if (paths.size>1) s"[$asList]" else asList)
  }

  protected def s(relation: String)  = if (paths.size==1) relation else relation + "s"

  private def getRelationForSingleXpath (path: Path, relation: String) = {
    if (path.getUnderlyingSource.isDefined || path.getXPath.isEmpty) throw new IllegalArgumentException("must use a pure xpath BasicPath")
    s"$relation::${path.getXPath.get}"
  }

  protected def getRelationXpath(relation: String) = {
    val result = List(paths:_*).map(path => {
      getRelationForSingleXpath(path, relation)
    }).mkString(" and ")
    s"$result"
  }

  private def rValueToString(path: Path): String = {
    if ((path.toString.trim.contains(" "))) "(" + path + ")" else path.toString
  }
}


