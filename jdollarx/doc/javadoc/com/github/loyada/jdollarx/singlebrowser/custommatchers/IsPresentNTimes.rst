.. java:import:: com.github.loyada.jdollarx InBrowser

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: com.github.loyada.jdollarx PathParsers

.. java:import:: com.github.loyada.jdollarx RelationOperator

.. java:import:: com.github.loyada.jdollarx.custommatchers CustomMatchersUtil

.. java:import:: com.github.loyada.jdollarx.custommatchers CustomMatchersUtil.NTimesMatcher

.. java:import:: com.github.loyada.jdollarx.singlebrowser InBrowserSinglton

.. java:import:: org.hamcrest Description

.. java:import:: org.hamcrest Matcher

.. java:import:: org.hamcrest TypeSafeMatcher

.. java:import:: org.openqa.selenium NoSuchElementException

.. java:import:: org.w3c.dom Document

.. java:import:: org.w3c.dom NodeList

.. java:import:: org.xml.sax SAXException

.. java:import:: javax.xml.parsers ParserConfigurationException

.. java:import:: javax.xml.xpath XPathExpressionException

.. java:import:: java.io IOException

.. java:import:: java.util Optional

IsPresentNTimes
===============

.. java:package:: com.github.loyada.jdollarx.singlebrowser.custommatchers
   :noindex:

.. java:type:: public class IsPresentNTimes

   This matcher is optimized for the success use-case. In that case it match for a single element with exact number of elements wanted. In case of failure, it will make another call to get the actual number of elements on the page, in order to provide a detailed error message. So the trade off is: In case of success it's faster, In case of failure it's slower. It makes sense since most of the time we expect success.

Constructors
------------
IsPresentNTimes
^^^^^^^^^^^^^^^

.. java:constructor:: public IsPresentNTimes(int nTimes)
   :outertype: IsPresentNTimes

Methods
-------
times
^^^^^

.. java:method:: public Matcher<Path> times()
   :outertype: IsPresentNTimes

timesOrLess
^^^^^^^^^^^

.. java:method:: public Matcher<Path> timesOrLess()
   :outertype: IsPresentNTimes

timesOrMore
^^^^^^^^^^^

.. java:method:: public Matcher<Path> timesOrMore()
   :outertype: IsPresentNTimes

