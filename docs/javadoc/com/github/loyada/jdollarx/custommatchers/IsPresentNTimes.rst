.. java:import:: com.github.loyada.jdollarx InBrowser

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: org.hamcrest Matcher

.. java:import:: org.w3c.dom Document

.. java:import:: org.xml.sax SAXException

.. java:import:: javax.xml.parsers ParserConfigurationException

.. java:import:: java.io IOException

IsPresentNTimes
===============

.. java:package:: com.github.loyada.jdollarx.custommatchers
   :noindex:

.. java:type:: public class IsPresentNTimes

   Internal implementation - not to be instantiated directly. This matcher is optimized for the success use-case. In that case it match for a single element with exact number of elements wanted. In case of failure, it will make another call to get the actual number of elements on the page, in order to provide a detailed error message. So the trade off is: In case of success it's faster, In case of failure it's slower. It makes sense since most of the time we expect success.

Constructors
------------
IsPresentNTimes
^^^^^^^^^^^^^^^

.. java:constructor:: public IsPresentNTimes(int nTimes)
   :outertype: IsPresentNTimes

Methods
-------
timesIn
^^^^^^^

.. java:method:: public Matcher<Path> timesIn(InBrowser browser)
   :outertype: IsPresentNTimes

timesIn
^^^^^^^

.. java:method:: public Matcher<Path> timesIn(Document doc) throws IOException, SAXException, ParserConfigurationException
   :outertype: IsPresentNTimes

timesOrLessIn
^^^^^^^^^^^^^

.. java:method:: public Matcher<Path> timesOrLessIn(InBrowser browser)
   :outertype: IsPresentNTimes

timesOrLessIn
^^^^^^^^^^^^^

.. java:method:: public Matcher<Path> timesOrLessIn(Document doc) throws IOException, SAXException, ParserConfigurationException
   :outertype: IsPresentNTimes

timesOrMoreIn
^^^^^^^^^^^^^

.. java:method:: public Matcher<Path> timesOrMoreIn(InBrowser browser)
   :outertype: IsPresentNTimes

timesOrMoreIn
^^^^^^^^^^^^^

.. java:method:: public Matcher<Path> timesOrMoreIn(Document doc) throws IOException, SAXException, ParserConfigurationException
   :outertype: IsPresentNTimes

