.. java:import:: com.github.loyada.jdollarx ElementProperty

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: com.github.loyada.jdollarx.singlebrowser InBrowserSinglton

.. java:import:: com.google.common.collect ImmutableList

.. java:import:: org.hamcrest Description

.. java:import:: org.hamcrest Matcher

.. java:import:: org.hamcrest TypeSafeMatcher

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.openqa.selenium WebElement

.. java:import:: java.util HashMap

.. java:import:: java.util List

.. java:import:: java.util Map

.. java:import:: java.util.concurrent TimeUnit

.. java:import:: java.util.stream IntStream

AgGrid
======

.. java:package:: com.github.loyada.jdollarx.singlebrowser.custommatchers
   :noindex:

.. java:type:: public class AgGrid

   Custom class to validate the presence of an AgGrid, since this can be tricky. It supports virtualized and non-virtualized tables. It should be used like other custom matchers in the package.

Methods
-------
getBuilder
^^^^^^^^^^

.. java:method:: public static AgGridBuilder getBuilder()
   :outertype: AgGrid

isPresent
^^^^^^^^^

.. java:method:: public static Matcher<AgGrid> isPresent()
   :outertype: AgGrid

   Verify that the grid, as defined, is present in the browser. In case of an assertion error, gives a useful error message. The assertion can be strict, in which case only the defined rows are expected to exist.

   :return: a Hamcrest matcher

toString
^^^^^^^^

.. java:method:: @Override public String toString()
   :outertype: AgGrid

