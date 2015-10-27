package info.dollarx

import info.dollarx.ElementProperties.has


object ElementPropertiesHelper {
  case class Or(p1: ElementProperty, p2: ElementProperty) extends ElementProperty {
    override def toString() = s"($p1 or $p2)"

    override def toXpath() = p1.toXpath + " or " + p2.toXpath

    override def or(p: ElementProperty) = Or(this, p)

    //I commented the line below, since it will transform logic like : [(A or B) and C]  to [A or (B and C)], which is wrong
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




  object HasHelper {
    import RelationOperator._

    private val countXpath : (String => String) = {(relation: String) => s"count($relation::* )" }
    private val countChildrenXpath = "count(./*)"
    private val countDescendantsXpath = countXpath("descendant")
    countXpath("descendant")
    private val countSiblingsXpath = s"${countXpath("preceding-sibling")} + ${countXpath("following-sibling")}"

    class HasChildren(n: Option[NCount] = None) extends ElementProperty {
      override def toXpath = if (n.isEmpty) s"$countChildrenXpath > 0" else {
        val count = n.get
        s"$countChildrenXpath${opAsXpathString(count.relationOperator)}${count.n}"
      }

      override def toString = {
        val number = if (n.isDefined) {
          val count = n.get
          (opAsEnglish(count.relationOperator) + count.n)
        } else " some"
        s"has$number children"
      }
    }

    class HasDescendants(n: Option[NCount] = None) extends ElementProperty {
      override def toXpath = if (n.isEmpty) s"$countChildrenXpath > 0" else {
        val count = n.get
        s"$countDescendantsXpath${opAsXpathString(count.relationOperator)}${count.n}"
      }

      override def toString = {
        val number = if (n.isDefined) {
          val count = n.get
          (opAsEnglish(count.relationOperator) + count.n)
        } else " some"
        s"has$number descendants"
      }
    }

    class HasSiblings(n: Option[NCount] = None) extends ElementProperty {
      override def toXpath = if (n.isEmpty) s"$countChildrenXpath > 0" else {
        val count = n.get
        s"$countSiblingsXpath${opAsXpathString(count.relationOperator)}${count.n}"
      }

      override def toString = {
        val number = if (n.isDefined) {
          val count = n.get
          (opAsEnglish(count.relationOperator) + count.n)
        } else " some"
        s"has$number siblings"
      }
    }

    object HasNoChildren extends ElementProperty{
      override def toXpath = s"$countChildrenXpath = 0"

      override def toString = "has no children"
    }

    case class HasN(n: NCount) {
      def children = new HasChildren(Some(n))
         def siblings = new HasSiblings(Some(n))
      def descendants = new HasDescendants(Some(n))

    }

    trait HasNotProperty {
      def get: ElementProperty
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




  case class hasText(txt: String) extends ElementProperty {
    override def toXpath() = XpathUtils.textEquals(txt)

    override def toString() = s"""has the text "${txt}""""
  }

  case class hasNoText(txt: String = "") extends ElementProperty {
    override def toXpath() = {
      val hasItProperty = if (txt=="") hasSomeText else hasText(txt)
      val hasNoProperty = Not(hasItProperty)
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

  trait IsProperty extends ElementProperty

  object IsHelpers {



    implicit def intToNPathBuilder(n: Int): NPathBuilder = NPathBuilder(n)

    object IsSiblingProperty extends ElementProperty with IsProperty{
      def apply(npath: NPath) = new ElementProperty {
        val path = npath.path
        val n = npath.n
        override def toXpath: String = s"count(following-sibling::${path.getXPath.get})+count(preceding-sibling::${path.getXPath.get})>=$n"
        override def toString: String = s"is a sibling of $n occurrences of $path"
      }
      def apply(paths: Path*) = isSibling(paths:_*)
      override def toXpath: String = ???
    }

    object IsAfterSiblingProperty extends ElementProperty with IsProperty{
      val relation = "preceding-sibling"
      def apply(npath: NPath) = new ElementProperty {
        val path = npath.path
        val n = npath.n
        override def toXpath: String = s"count($relation::${path.getXPath.get})>=$n"
        override def toString: String = s"is after $n occurrences of $path siblings"
      }
      def apply(paths: Path*) = isAfterSibling(paths:_*)
      override def toXpath: String = ???
    }
    object IsBeforeSiblingProperty extends ElementProperty  with IsProperty{
      val relation = "following-sibling"
      def apply(npath: NPath) = new ElementProperty {
        val path = npath.path
        val n = npath.n
        override def toXpath: String = s"count($relation::${path.getXPath.get})>=$n"
        override def toString: String = s"is before $n occurrences of $path siblings"
      }
      def apply(paths: Path*) = isBeforeSibling(paths:_*)
      override def toXpath: String = ???
    }

    object IsAfterProperty extends ElementProperty with IsProperty{
      val relation = "preceding"
      def apply(npath: NPath) = new ElementProperty with IsProperty{
        val path = npath.path
        val n = npath.n
        override def toXpath: String = s"count($relation::${path.getXPath.get})>=$n"
        override def toString: String = s"is after $n occurrences of $path"
      }
      def apply(paths: Path*) = isAfter(paths:_*)
      override def toXpath: String = ???
    }

    object IsBeforeProperty extends ElementProperty with IsProperty{
      val relation = "following"
      def apply(npath: NPath) = new ElementProperty with IsProperty{
        val path = npath.path
        val n = npath.n
        override def toXpath: String = s"count($relation::${path.getXPath.get})>=$n"
        override def toString: String = s"is before $n occurrences of $path"
      }
      def apply(paths: Path*) = isBefore(paths:_*)
      override def toXpath: String = ???
    }
  }

  case class NPath(n: Int, path: Path)
  case class NPathBuilder(n: Int) {
    object occurrencesOf {
      def apply(path: Path) = NPath(n, path)
    }
  }

  implicit def intToNPathBuilder(n: Int): NPathBuilder = NPathBuilder(n)

  private def rValueToString(path: Path): String = {
    if ((path.toString.trim.contains(" "))) "(" + path + ")" else path.toString
  }

  case class isAfter(paths: Path*) extends ElementProperty with relationBetweenMultiElement with IsProperty {
    protected val relation = "preceding"
    override def toString() = asString("is after")
    override protected def plural(relation: String)  = relation
  }

  case class isSibling(paths: Path*) extends ElementProperty with relationBetweenMultiElement with IsProperty{
    protected val relation = ""
    override protected def getXpathExpressionForSingle(path: Path): String = (has sibling(path)).toXpath
    override def toString() = asString("has sibling")
  }

  case class isAfterSibling(paths: Path*) extends ElementProperty with relationBetweenMultiElement with IsProperty{
    protected val relation = "preceding-sibling"
    override def toString() = asString("is after sibling")
  }

  case class isBefore(paths: Path*) extends ElementProperty with relationBetweenMultiElement with IsProperty{
    protected val relation = "following"
    override def toString() = asString("is before")
  }

  case class isBeforeSibling(paths: Path*) extends ElementProperty with relationBetweenMultiElement with IsProperty{
    protected val relation = "following-sibling"
    override def toString() = asString("is before sibling")
  }

  case class  IsWithIndexInRange(first: Int, last: Int) extends ElementProperty with IsProperty{
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
    s"${plural(prefix)}: " + (if (paths.size>1) s"[$asList]" else asList)
  }

  protected def plural(relation: String)  = if (paths.size==1) relation else relation + "s"

  protected def getXpathExpressionForSingle(path: Path) = s"$relation::${path.getXPath.get}"


  private def getRelationForSingleXpath (path: Path, relation: String) = {
    if (path.getUnderlyingSource.isDefined || path.getXPath.isEmpty) throw new IllegalArgumentException("must use a pure xpath BasicPath")
    getXpathExpressionForSingle(path)
  }

  protected def getRelationXpath(relation: String) = {
    val result = List(paths:_*).map(path => {
      getRelationForSingleXpath(path, relation)
    }).mkString(") and (")
    if (paths.length>1)  s"($result)" else s"$result"
  }

  private def rValueToString(path: Path): String = {
    if ((path.toString.trim.contains(" "))) "(" + path + ")" else path.toString
  }
}
