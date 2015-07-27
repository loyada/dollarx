package info.dollarx


trait ElementProperty {

    def toXpath: String

    def or(p: ElementProperty): ElementProperty = {
      return new ElementProperties.Or(this, p)
    }

    def and(prop: ElementProperty): ElementProperty = {
      return new ElementProperties.And(this, prop)
    }

    def andNot(prop: ElementProperty): ElementProperty = {
      return and(new ElementProperties.Not(prop))
    }
}
