.. java:import:: com.github.loyada.jdollarx InBrowser

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: com.github.loyada.jdollarx RelationOperator

.. java:import:: org.hamcrest Description

.. java:import:: org.hamcrest Matcher

.. java:import:: org.hamcrest TypeSafeMatcher

.. java:import:: org.openqa.selenium NoSuchElementException

HasElementNTimes.NTimesMatcher
==============================

.. java:package:: com.github.loyada.jdollarx.custommatchers
   :noindex:

.. java:type:: public static class NTimesMatcher extends TypeSafeMatcher<InBrowser>
   :outertype: HasElementNTimes

Fields
------
foundNTimes
^^^^^^^^^^^

.. java:field::  int foundNTimes
   :outertype: HasElementNTimes.NTimesMatcher

Constructors
------------
NTimesMatcher
^^^^^^^^^^^^^

.. java:constructor::  NTimesMatcher(Path path, int nTimes, RelationOperator relationOperator)
   :outertype: HasElementNTimes.NTimesMatcher

Methods
-------
describeMismatchSafely
^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: @Override protected void describeMismatchSafely(InBrowser browser, Description mismatchDescription)
   :outertype: HasElementNTimes.NTimesMatcher

describeTo
^^^^^^^^^^

.. java:method:: @Override public void describeTo(Description description)
   :outertype: HasElementNTimes.NTimesMatcher

matchesSafely
^^^^^^^^^^^^^

.. java:method:: @Override protected boolean matchesSafely(InBrowser browser)
   :outertype: HasElementNTimes.NTimesMatcher

