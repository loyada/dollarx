.. java:import:: org.w3c.dom Document

.. java:import:: org.w3c.dom NodeList

.. java:import:: org.xml.sax SAXException

.. java:import:: javax.xml.parsers DocumentBuilder

.. java:import:: javax.xml.parsers DocumentBuilderFactory

.. java:import:: javax.xml.parsers ParserConfigurationException

.. java:import:: java.io ByteArrayInputStream

.. java:import:: java.io IOException

.. java:import:: java.io InputStream

.. java:import:: java.nio.charset StandardCharsets

PathParsers
===========

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public final class PathParsers

   Internal implementation.

Methods
-------
findAllByPath
^^^^^^^^^^^^^

.. java:method:: public static NodeList findAllByPath(String docString, Path path) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException
   :outertype: PathParsers

findAllByPath
^^^^^^^^^^^^^

.. java:method:: public static NodeList findAllByPath(Document doc, Path path) throws XPathExpressionException
   :outertype: PathParsers

findAllByXPath
^^^^^^^^^^^^^^

.. java:method:: public static NodeList findAllByXPath(Document doc, String extractedXpath) throws XPathExpressionException
   :outertype: PathParsers

getDocumentFromString
^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static Document getDocumentFromString(String exampleString) throws ParserConfigurationException, IOException, SAXException
   :outertype: PathParsers

