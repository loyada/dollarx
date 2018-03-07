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

CustomMatchersUtil.ISPresentNTimesMatcherForDocument
====================================================

.. java:package:: com.github.loyada.jdollarx.custommatchers
   :noindex:

.. java:type:: public static class ISPresentNTimesMatcherForDocument extends TypeSafeMatcher<Path>
   :outertype: CustomMatchersUtil

Fields
------
foundNTimes
^^^^^^^^^^^

.. java:field::  int foundNTimes
   :outertype: CustomMatchersUtil.ISPresentNTimesMatcherForDocument

Constructors
------------
ISPresentNTimesMatcherForDocument
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:constructor:: public ISPresentNTimesMatcherForDocument(int nTimes, RelationOperator relationOperator, Document doc) throws ParserConfigurationException, IOException, SAXException
   :outertype: CustomMatchersUtil.ISPresentNTimesMatcherForDocument

Methods
-------
describeMismatchSafely
^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: @Override protected void describeMismatchSafely(Path el, Description mismatchDescription)
   :outertype: CustomMatchersUtil.ISPresentNTimesMatcherForDocument

describeTo
^^^^^^^^^^

.. java:method:: @Override public void describeTo(Description description)
   :outertype: CustomMatchersUtil.ISPresentNTimesMatcherForDocument

matchesSafely
^^^^^^^^^^^^^

.. java:method:: @Override protected boolean matchesSafely(Path el)
   :outertype: CustomMatchersUtil.ISPresentNTimesMatcherForDocument

