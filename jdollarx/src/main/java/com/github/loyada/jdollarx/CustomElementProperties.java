package com.github.loyada.jdollarx;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class CustomElementProperties {

    private CustomElementProperties() {}

    /**
     * Easy way to define a custom property generator that accepts a single parameter.
     * For example:
     * {@code Function<String, ElementProperty> dataFoo = createPropertyFunc(
     *                       fooVal -> String.format("[@data-foo=%s]", fooVal),
     *                       fooVal -> "has Foo " + fooVal );}
     * @param xpathCreator function that generates the xpath that represents this attribute
     * @param toStringCreator function that generates the string (text) representation of this attribute
     * @param <T> the type of the parameter the above functions expect
     * @return a function that generates an ElementProperty. Best used
     */
    public static  <T> Function<T, ElementProperty> createPropertyGenerator(
            Function<T,String> xpathCreator,
            Function<T,String> toStringCreator){
        return t -> new ElementProperty() {
            @Override
            public String toXpath() {
                return xpathCreator.apply(t);
            }

            public String toString() {
                return toStringCreator.apply(t);
            }

        };
    }

    public static  <T, V> BiFunction<T, V, ElementProperty> createPropertyGenerator(
            BiFunction<T, V, String> xpathCreator,
            BiFunction<T,V, String> toStringCreator){
        return (T t, V v)-> new ElementProperty() {
            @Override
            public String toXpath() {
                return xpathCreator.apply(t, v);
            }

            public String toString() {
                return toStringCreator.apply(t, v);
            }

        };
    }

    /**
     * Syntactic sugar that allows to define properties of the form:
     * role = createPropertyGenerator(....)
     * element.that(hasProperty(role, "foo"))
     *
     * @param propertyGen a function that was generated using createPropertyGenerator()
     * @param t - a parameter the property generator expects
     * @param <T> the type of the value to match to
     * @return Element property
     */
    public static <T> ElementProperty hasProperty(Function<T, ElementProperty> propertyGen, T t) {
        return propertyGen.apply(t);
    }

    /**
     *
     * @param propertyGen a function that was generated using createPropertyGenerator()
     * @param t - first parameter the property generator expects
     * @param v - first parameter the property generator expects
     * @param <T> the type of the first parameter (t)
     * @param <V> the type of the second parameter (v)
     * @return Element property
     */
    public static <T, V> ElementProperty hasProperty(BiFunction<T, V, ElementProperty> propertyGen, T t, V v) {
        return propertyGen.apply(t, v);
    }

}
