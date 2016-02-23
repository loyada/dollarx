package com.github.loyada.dollarx

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath._

import org.w3c.dom.{Document, NodeList}

object PathParsers {

  def getDocumentFromString(exampleString: String): Document = {
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val input = new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8))
    builder.parse(input)
  }

  def findAllByPath(docString: String, path: Path): NodeList = findAllByPath(getDocumentFromString(docString), path)

  def findAllByPath(doc: Document, path: Path): NodeList = {
    findAllByXPath(doc, path.getXPath.get)
  }

  def findAllByXPath(doc: Document, extractedXpath: String): NodeList = {
    val xPathfactory: XPathFactory = XPathFactory.newInstance
    val xpath: XPath = xPathfactory.newXPath
    val expr: XPathExpression = xpath.compile(XpathUtils.insideTopLevel(extractedXpath))
    expr.evaluate(doc, XPathConstants.NODESET).asInstanceOf[NodeList]
  }
}
