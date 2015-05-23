package dollarx

import dollarx.ElementProperties.{last, index}


object ElementProperties {

  case class Or(p1: ElementProperties, p2: ElementProperties) extends ElementProperties {
    override def toString() = p1.toString + " or " + p2.toString

    override def or(p: ElementProperties) = Or(this, p)

    //we commented the line below, since it will transform logic like : [(A or B) and C]  to [A or (B and C)], which is bad
    //override def and(p: ElementProperties) = Or(p1, And(p2, p))
  }

  case class And(p1: ElementProperties, p2: ElementProperties) extends ElementProperties {
    override def toString() = {
      "(" + p1.toString + " and " + p2.toString + ")"
    }
  }

  case class Not(p: ElementProperties) extends ElementProperties {
    override def toString() = "not(" + p.toString + ")"
  }

  case class hasClass(cssClass: String) extends ElementProperties {
    override def toString = XpathUtils.hasClass(cssClass)
  }

  case class hasId(cssClass: String) extends ElementProperties {
    override def toString = XpathUtils.hasId(cssClass)
  }

  case class hasOneOfClasses(cssClasses: String*) extends ElementProperties {
    override def toString = XpathUtils.hasOneOfClasses(cssClasses:_*)
  }

  case class index(n: Int, p: ElementProperties = null) extends ElementProperties {
    override def toString() = (if (p != null) "(" + p + ")" else "") + s"[${n + 1}]"
  }

  case class last(p: ElementProperties = null) extends ElementProperties {
    override def toString() = (if (p != null) "(" + p + ")" else "") + "[last()]"
  }

  case class raw(rawXpathProps: String) extends ElementProperties {
    override def toString() = rawXpathProps
  }

  case class hasText(txt: String) extends ElementProperties {
    override def toString() = XpathUtils.textEquals(txt)
  }

  case class hasTextContaining(txt: String) extends ElementProperties {
    override def toString() = XpathUtils.textContains(txt)
  }

  case class withAggregatedTextEqualTo(txt: String) extends ElementProperties {
    override def toString() = XpathUtils.aggregatedTextEquals(txt)
  }

  case class withAggregatedTextContaining(txt: String) extends ElementProperties {
    override def toString() = XpathUtils.aggregatedTextContains(txt)
  }

  object withSomeText extends ElementProperties {
    override def toString() = XpathUtils.hasSomeText
  }

  object isHidden extends ElementProperties {
    override def toString() = XpathUtils.isHidden
  }

  case class childOf(webel: WebEl) extends ElementProperties {
    override def toString() = {
      if (webel.getUnderlyingSource().isDefined || webel.getXPath().isEmpty) throw new IllegalArgumentException("must use a pure xpath WebEl")
      val xpath = webel.getXPath().get
      "parent::" + xpath
    }
  }

  case class hasDecendant(webEl: WebEl) extends ElementProperties {
    override def toString() = {
      if (webEl.getUnderlyingSource().isDefined || webEl.getXPath().isEmpty) throw new IllegalArgumentException("must use a pure xpath WebEl")
      "descendant::" + webEl.getXPath().get
    }
  }

  case class hasAncesctor(webEl: WebEl) extends ElementProperties {
    override def toString() = {
      if (webEl.getUnderlyingSource().isDefined || webEl.getXPath().isEmpty) throw new IllegalArgumentException("must use a pure xpath WebEl")
      "ancestor::" + webEl.getXPath().get
    }
  }

  case class hasParent(webEl: WebEl) extends ElementProperties {
    override def toString() = {
      if (webEl.getUnderlyingSource().isDefined || webEl.getXPath().isEmpty) throw new IllegalArgumentException("must use a pure xpath WebEl")
      "parent::" + webEl.getXPath().get
    }
  }
  case class hasChild(webEl: WebEl) extends ElementProperties {
    override def toString() = {
      if (webEl.getUnderlyingSource().isDefined || webEl.getXPath().isEmpty) throw new IllegalArgumentException("must use a pure xpath WebEl")
      "child::" + webEl.getXPath().get
    }
  }
  case class isfter(webEl: WebEl) extends ElementProperties {
    override def toString() = {
      if (webEl.getUnderlyingSource().isDefined || webEl.getXPath().isEmpty) throw new IllegalArgumentException("must use a pure xpath WebEl")
      "preceding::" + webEl.getXPath().get
    }
  }

  case class isAfterSibling(webEl: WebEl) extends ElementProperties {
    override def toString() = {
      if (webEl.getUnderlyingSource().isDefined || webEl.getXPath().isEmpty) throw new IllegalArgumentException("must use a pure xpath WebEl")
      "preceding-sibling::" + webEl.getXPath().get
    }
  }

  case class isBefore(webEl: WebEl) extends ElementProperties {
    override def toString() = {
      if (webEl.getUnderlyingSource().isDefined || webEl.getXPath().isEmpty) throw new IllegalArgumentException("must use a pure xpath WebEl")
      "following::" + webEl.getXPath().get
    }
  }

  case class isBeforeSibling(webEl: WebEl) extends ElementProperties {
    override def toString() = {
      if (webEl.getUnderlyingSource().isDefined || webEl.getXPath().isEmpty) throw new IllegalArgumentException("must use a pure xpath WebEl")
      "following-sibling::" + webEl.getXPath().get
    }
  }

}

trait ElementProperties {
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

