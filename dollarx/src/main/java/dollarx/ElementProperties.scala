package dollarx

import dollarx.ElementProperties.{last, index}


object ElementProperties {

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

  case class hasId(id: String) extends ElementProperties {
    override def toXpath = XpathUtils.hasId(id)

    override def toString = s"has Id $id"
  }

  case class hasOneOfClasses(cssClasses: String*) extends ElementProperties {
    override def toXpath = XpathUtils.hasOneOfClasses(cssClasses: _*)

    override def toString = s"has at least one of classes: [${cssClasses.mkString(", ")}"
  }

  case class index(n: Int, p: ElementProperties = null) extends ElementProperties {
    override def toXpath() = (if (p != null) "(" + p.toXpath + ")" else "") + s"[${n + 1}]"

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

  case class isfter(webEl: WebEl) extends ElementProperties with relationBetweenElement {
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

