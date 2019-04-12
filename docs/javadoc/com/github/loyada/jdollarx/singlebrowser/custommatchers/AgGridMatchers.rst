.. java:import:: com.github.loyada.jdollarx.singlebrowser AgGrid

.. java:import:: org.hamcrest Description

.. java:import:: org.hamcrest Matcher

.. java:import:: org.hamcrest TypeSafeMatcher

.. java:import:: org.openqa.selenium NoSuchElementException

AgGridMatchers
==============

.. java:package:: com.github.loyada.jdollarx.singlebrowser.custommatchers
   :noindex:

.. java:type:: public class AgGridMatchers

   Hamcrest matchers for an AgGrid

Methods
-------
isPresent
^^^^^^^^^

.. java:method:: public static Matcher<AgGrid> isPresent()
   :outertype: AgGridMatchers

   Verify that the grid, as defined, is present in the browser. In case of an assertion error, gives a useful error message. The assertion can be strict, in which case only the defined rows are expected to exist.

   :return: a Hamcrest matcher

