package com.github.loyada.dollarx

/**
  * Created by danny on 2/6/16.
  */
trait ElementProperty {

    def toXpath: String

    def or(p: ElementProperty): ElementProperty = {
      return new ElementPropertiesHelper.Or(this, p)
    }

    def and(prop: ElementProperty): ElementProperty = {
      return new ElementPropertiesHelper.And(this, prop)
    }

    def andNot(prop: ElementProperty): ElementProperty = {
      return and(new ElementPropertiesHelper.Not(prop))
    }
}
