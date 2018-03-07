package com.github.loyada.jdollarx;


/**
 * Used to define a constraint on a number of elements
 */
public interface ElementPropertyWithNumericalBoundaries extends ElementProperty{
    /**
     * Given the a property and and a count of it, returns a property equivalent to at least the count of that property.
     * This works only with specific properties.
     *
     * For example:
     * div.that(hasNChildren(2).orMore())
     *
     * @return a property equivalent to at least the count of that property
     */
    ElementProperty orMore();

    /**
     * Given the a property and and a count of it, returns a property equivalent to at most the count of that property.
     * This works only with specific properties.
     *
     * For example:
     * div.that(hasNChildren(2).orLess())
     *
     * @return a property equivalent to at most the count of that property
     */
    ElementProperty orLess();
}
