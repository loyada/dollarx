.. java:import:: org.w3c.dom Document

.. java:import:: org.w3c.dom Node

.. java:import:: javax.xml.parsers DocumentBuilder

.. java:import:: javax.xml.parsers DocumentBuilderFactory

.. java:import:: javax.xml.parsers ParserConfigurationException

.. java:import:: java.util HashMap

.. java:import:: java.util Map

DOMBuilder
==========

.. java:package:: com.github.loyada.jdollarx.utils
   :noindex:

.. java:type:: final class DOMBuilder

   :author: \ `Walter Kasper <mailto:kasper@dfki.de>`_\

Methods
-------
jsoup2DOM
^^^^^^^^^

.. java:method:: public static Document jsoup2DOM(org.jsoup.nodes.Document jsoupDocument)
   :outertype: DOMBuilder

   Returns a W3C DOM that exposes the same content as the supplied Jsoup document into a W3C DOM.

   :param jsoupDocument: The Jsoup document to convert.
   :return: A W3C Document.

