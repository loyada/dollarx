.. java:import:: java.util.function BiFunction

.. java:import:: java.util.function Function

CustomElementProperties
=======================

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public final class CustomElementProperties

   Functions to create custom \ :java:ref:`ElementProperty`\ , if the property is unsupported out-of-the-box.

Methods
-------
createPropertyGenerator
^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static <T> Function<T, ElementProperty> createPropertyGenerator(Function<T, String> xpathCreator, Function<T, String> toStringCreator)
   :outertype: CustomElementProperties

   Easy way to define a custom property generator that accepts a single parameter. For example:

   .. parsed-literal::

      Function<String, ElementProperty> dataFoo = createPropertyFunc(
                            fooVal -> String.format("[@data-foo=%s]", fooVal),
                            fooVal -> "has Foo " + fooVal );
             Path myDataEl = element.that(hasProperty(dataFoo("bar")));

   :param xpathCreator: function that generates the xpath that represents this attribute
   :param toStringCreator: function that generates the string (text) representation of this attribute
   :param <T>: the type of the parameter the above functions expect
   :return: a function that generates an ElementProperty. Use with \ :java:ref:`hasProperty(Function,Object)`\

createPropertyGenerator
^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static <T, V> BiFunction<T, V, ElementProperty> createPropertyGenerator(BiFunction<T, V, String> xpathCreator, BiFunction<T, V, String> toStringCreator)
   :outertype: CustomElementProperties

   Easy way to define a custom property generator that accepts two parameter. For example:

   .. parsed-literal::

      Function<String, ElementProperty> dataAttr = createPropertyFunc(
                           (attr, val) -> String.format("[@data-%s=%s]", attr, val),
                           (attr, val) -> String.format("has data-%s of %s", attr, val );
            Path myDataEl = element.that(hasProperty(dataAttr("foo", "bar")));

   :param xpathCreator: function that generates the xpath that represents this attribute
   :param toStringCreator: function that generates the string (text) representation of this attribute
   :param <T>: the type of the first parameter the above functions expect
   :param <V>: the type of the second parameter the above functions expect
   :return: a function that generates an ElementProperty. Use with \ :java:ref:`hasProperty(BiFunction,Object,Object)`\

hasProperty
^^^^^^^^^^^

.. java:method:: public static <T> ElementProperty hasProperty(Function<T, ElementProperty> propertyGen, T t)
   :outertype: CustomElementProperties

   Syntactic sugar that allows to define properties of the form:

   .. parsed-literal::

       role = createPropertyGenerator(....);
      element.that(hasProperty(role, "foo"));

   :param propertyGen: a function that was generated using createPropertyGenerator()
   :param t: - a parameter the property generator expects
   :param <T>: the type of the value to match to
   :return: Element property

hasProperty
^^^^^^^^^^^

.. java:method:: public static <T, V> ElementProperty hasProperty(BiFunction<T, V, ElementProperty> propertyGen, T t, V v)
   :outertype: CustomElementProperties

   :param propertyGen: a function that was generated using createPropertyGenerator()
   :param t: - first parameter the property generator expects
   :param v: - first parameter the property generator expects
   :param <T>: the type of the first parameter (t)
   :param <V>: the type of the second parameter (v)
   :return: Element property

