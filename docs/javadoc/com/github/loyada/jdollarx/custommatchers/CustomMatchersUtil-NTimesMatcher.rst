.. java:import:: com.github.loyada.jdollarx InBrowser

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: com.github.loyada.jdollarx PathParsers

.. java:import:: com.github.loyada.jdollarx RelationOperator

.. java:import:: org.hamcrest Description

.. java:import:: org.hamcrest TypeSafeMatcher

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.w3c.dom Document

.. java:import:: org.w3c.dom NodeList

.. java:import:: org.xml.sax SAXException

.. java:import:: javax.xml.parsers ParserConfigurationException

.. java:import:: javax.xml.xpath XPathExpressionException

.. java:import:: java.io IOException

.. java:import:: java.util Optional

CustomMatchersUtil.NTimesMatcher
================================

.. java:package:: com.github.loyada.jdollarx.custommatchers
   :noindex:

.. java:type:: public static class NTimesMatcher extends TypeSafeMatcher<Path>
   :outertype: CustomMatchersUtil

   Internal implementation

Fields
------
foundNTimes
^^^^^^^^^^^

.. java:field::  int foundNTimes
   :outertype: CustomMatchersUtil.NTimesMatcher

Constructors
------------
NTimesMatcher
^^^^^^^^^^^^^

.. java:constructor:: public NTimesMatcher(int nTimes, RelationOperator relationOperator, InBrowser browser)
   :outertype: CustomMatchersUtil.NTimesMatcher

Methods
-------
describeMismatchSafely
^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: @Override protected void describeMismatchSafely(Path el, Description mismatchDescription)
   :outertype: CustomMatchersUtil.NTimesMatcher

describeTo
^^^^^^^^^^

.. java:method:: @Override public void describeTo(Description description)
   :outertype: CustomMatchersUtil.NTimesMatcher

matchesSafely
^^^^^^^^^^^^^

.. java:method:: @Override protected boolean matchesSafely(Path el)
   :outertype: CustomMatchersUtil.NTimesMatcher

toString
^^^^^^^^

.. java:method:: @Override public String toString()
   :outertype: CustomMatchersUtil.NTimesMatcher

