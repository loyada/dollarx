.. java:import:: com.github.loyada.jdollarx InBrowser

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: com.github.loyada.jdollarx RelationOperator

.. java:import:: org.hamcrest Description

.. java:import:: org.hamcrest Matcher

.. java:import:: org.hamcrest TypeSafeMatcher

.. java:import:: org.openqa.selenium NoSuchElementException

HasElementNTimes
================

.. java:package:: com.github.loyada.jdollarx.custommatchers
   :noindex:

.. java:type:: public class HasElementNTimes

   This matcher is optimized for the success use-case. In that case it match for a single element with exact number of elements wanted. In case of failure, it will make another call to get the actual number of elements on the page, in order to provide a detailed error message. So the trade off is: In case of success it's faster, In case of failure it's slower. It makes sense since most of the time we expect success.

Constructors
------------
HasElementNTimes
^^^^^^^^^^^^^^^^

.. java:constructor:: public HasElementNTimes(Path path, int nTimes)
   :outertype: HasElementNTimes

Methods
-------
times
^^^^^

.. java:method:: public Matcher<InBrowser> times()
   :outertype: HasElementNTimes

timesOrLess
^^^^^^^^^^^

.. java:method:: public Matcher<InBrowser> timesOrLess()
   :outertype: HasElementNTimes

timesOrMore
^^^^^^^^^^^

.. java:method:: public Matcher<InBrowser> timesOrMore()
   :outertype: HasElementNTimes

