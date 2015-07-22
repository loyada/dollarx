package org.lab49.jdollarx;


public interface ElementProperty {
    String toXpath() ;

    default public ElementProperty or(ElementProperty p) {
        return new ElementProperties.Or(this, p);
    }

    default public ElementProperty and(ElementProperty prop) {
        return new ElementProperties.And(this, prop);
    }

    default public ElementProperty andNot(ElementProperty prop) { return and(new ElementProperties.Not(prop)); }
}
