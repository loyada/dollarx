package info.dollarx


import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath._

import org.w3c.dom.{Document, NodeList}

object PathParsers {

  def setupHTMLFromString(exampleString: String): Document = {
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val input = new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8))
    builder.parse(input)
  }

  def findAllByPath(docString: String, path: Path): NodeList = findAllByPath(setupHTMLFromString(docString), path)

  def findAllByPath(doc: Document, path: Path): NodeList = {
    val extractedXpath = path.getXPath.get
    val xPathfactory = XPathFactory.newInstance()
    val xpath = xPathfactory.newXPath()
    val prefix = if (extractedXpath.startsWith("/") || extractedXpath.startsWith("(/")) ""
    else if (extractedXpath.startsWith("(")) "(//" else "//"
    val chopn = if (extractedXpath.startsWith("(") && !extractedXpath.startsWith("(/")) 1 else 0
    val expr = xpath.compile(prefix + extractedXpath.substring(chopn))
    expr.evaluate(doc, XPathConstants.NODESET).asInstanceOf[NodeList]
  }

}
