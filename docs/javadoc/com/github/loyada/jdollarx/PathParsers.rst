.. java:import:: org.w3c.dom Document

.. java:import:: org.w3c.dom NodeList

.. java:import:: org.xml.sax SAXException

.. java:import:: javax.xml.parsers DocumentBuilder

.. java:import:: javax.xml.parsers DocumentBuilderFactory

.. java:import:: javax.xml.parsers ParserConfigurationException

.. java:import:: javax.xml.xpath XPath

.. java:import:: javax.xml.xpath XPathConstants

.. java:import:: javax.xml.xpath XPathExpression

.. java:import:: javax.xml.xpath XPathExpressionException

.. java:import:: javax.xml.xpath XPathFactory

.. java:import:: java.io ByteArrayInputStream

.. java:import:: java.io IOException

.. java:import:: java.io InputStream

.. java:import:: java.nio.charset StandardCharsets

PathParsers
===========

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public final class PathParsers

   functions to find DOM elements in a W3C document. These functions are also useful to experiment and test with how Paths can be used to extract elements (they are used in many of the unit tests of DollarX).

   .. parsed-literal::

      Example use:
      Path el = div.before(span);
          String xpath = el.getXPath().get();
          NodeList nodes = findAllByXpath("<div>foo</div><div>boo</div><span></span>", el);
          assertThat(nodes.getLength(), is(2));
          assertThat(nodes.item(0).getTextContent(), equalTo("foo"));

Methods
-------
findAllByPath
^^^^^^^^^^^^^

.. java:method:: public static NodeList findAllByPath(String docString, Path path) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException
   :outertype: PathParsers

   find all the nodes that match a path in a W3C document

   :param docString: a W3C document
   :param path: the path to find.
   :return: a node list with the details of all the elements that match the given path

findAllByPath
^^^^^^^^^^^^^

.. java:method:: public static NodeList findAllByPath(Document doc, Path path) throws XPathExpressionException
   :outertype: PathParsers

   find all the nodes that match a path in a W3C document

   :param doc: a W3C document
   :param path: the path to find
   :return: a node list with the details of all the elements that match the given path

findAllByXPath
^^^^^^^^^^^^^^

.. java:method:: public static NodeList findAllByXPath(Document doc, String extractedXpath) throws XPathExpressionException
   :outertype: PathParsers

   internal implementation

   :param doc: a W3C document
   :param extractedXpath: an xpath
   :return: a node list with the details of all the elements that match the given xpath

getDocumentFromString
^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static Document getDocumentFromString(String document) throws ParserConfigurationException, IOException, SAXException
   :outertype: PathParsers

   Convert a string to a \ :java:ref:`Document`\ , Assuming utf-8 encoding.

   :param document: the document as a string
   :return: the document as a @link Document}

