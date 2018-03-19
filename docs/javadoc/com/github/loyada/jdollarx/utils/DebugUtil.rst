.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: org.jsoup Jsoup

.. java:import:: org.jsoup.nodes Element

.. java:import:: org.openqa.selenium JavascriptExecutor

.. java:import:: org.openqa.selenium WebElement

.. java:import:: java.util List

.. java:import:: java.util Optional

.. java:import:: java.util.stream Collectors

DebugUtil
=========

.. java:package:: com.github.loyada.jdollarx.utils
   :noindex:

.. java:type:: public final class DebugUtil

   Several utilities that are useful for troubleshooting of existing browser pages. The utilities assume the use of \ :java:ref:`com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton`\ .

Methods
-------
getDOM
^^^^^^

.. java:method:: public static Optional<Element> getDOM(Path el)
   :outertype: DebugUtil

   Same as \ :java:ref:`getDOMOfAll(Path)`\ , but returns an optional of the first match.

   :param el: the path we are looking for
   :return: the first Element that matches the path in the current page

getDOMOfAll
^^^^^^^^^^^

.. java:method:: public static List<Element> getDOMOfAll(Path el)
   :outertype: DebugUtil

   Get all matches of the path as a list of \ :java:ref:`Element`\ . JSoup \ :java:ref:`Element`\  are a nice, readable way to examine DOM objects. This is useful for troubleshooting. This method relies on \ :java:ref:`com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton`\ .

   :param el: the path we are looking for
   :return: all the elements that match it in the current page

getPageAsW3CDoc
^^^^^^^^^^^^^^^

.. java:method:: public static org.w3c.dom.Document getPageAsW3CDoc()
   :outertype: DebugUtil

   Download the current page and convert it to a W3C Document, which can be inspected using the \ :java:ref:`com.github.loyada.jdollarx.PathParsers`\  methods

   :return: a W3C document

