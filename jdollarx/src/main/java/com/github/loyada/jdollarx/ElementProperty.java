package com.github.loyada.jdollarx;


public interface ElementProperty {
    String toXpath() ;

    /**
     * returns a new property, that is a combination of the current property OR the given property parameter. Meaning the element
     * is required to have any of the two properties.
     * Obviously, this can be used multiple times: prop1.or(prop2).or(prop3).and(prop4)
     *
     * @param prop another property to perform a logical "OR" with
     * @return a new property that is equivalent to:  (this property OR prop)
     */
    default ElementProperty or(ElementProperty prop) {
        return new ElementProperties.Or(this, prop);
    }

    /**
     * returns a new property, that is a combination of the current property AND the given property parameter. Meaning the element
     * is required to have both properties.
     * Obviously, this can be used multiple times: prop1.and(prop2).or(prop3).and(prop4)
     *
     * @param prop another property to perform a logical "AND" with
     * @return a new property that is equivalent to:  (this property AND prop)
     */
    default ElementProperty and(ElementProperty prop) {
        return new ElementProperties.And(this, prop);
    }

    /**
     * returns a new property, that is equivalent to the current property, BUT NOT the property parameter.
     * Obviously, this can be used multiple times: prop1.andNot(prop2).or(prop3.andNot(prop4))
     *
     * @param prop another property to perform a logical "NAND" with
     * @return a new property that is equivalent to: (this property and not prop)
     */
    default ElementProperty andNot(ElementProperty prop) { return and(new ElementProperties.Not(prop)); }
}
