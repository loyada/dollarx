package com.github.loyada.jdollarx;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * functions to find DOM elements in a W3C document. These functions are also useful to experiment and test with how Paths
 * can be used to extract elements (they are used in many of the unit tests of DollarX).
 * <pre>Example use:
 * {@code
 *     Path el = div.before(span);
 *     String xpath = el.getXPath().get();
 *     NodeList nodes = findAllByXpath("<div>foo</div><div>boo</div><span></span>", el);
 *     assertThat(nodes.getLength(), is(2));
 *     assertThat(nodes.item(0).getTextContent(), equalTo("foo"));
 * }
 * </pre>
 */
public final class PathParsers {
    private PathParsers(){}

    /**
     * Convert a string to a {@link Document}, Assuming utf-8 encoding.
     * @param document the document as a string
     * @return the document as a @link Document}
     */
    public static Document getDocumentFromString(final String document) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream input = new ByteArrayInputStream(document.getBytes(StandardCharsets.UTF_8));
        return builder.parse(input);
    }

    /**
     * find all the nodes that match a path in a W3C document
     * @param docString a W3C document
     * @param path the path to find.
     * @return a node list with the details of all the elements that match the given path
     */
    public static NodeList findAllByPath(final String docString, final Path path) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
        return findAllByPath(getDocumentFromString(docString), path );
    }

    /**
     * find all the nodes that match a path in a W3C document
     * @param doc a W3C document
     * @param path the path to find
     * @return a node list with the details of all the elements that match the given path
     */
    public static NodeList findAllByPath(final Document doc, final Path path) throws XPathExpressionException {
       return findAllByXPath(doc, path.getXPath().get());
    }

    /**
     * internal implementation
     * @param doc a W3C document
     * @param extractedXpath an xpath
     * @return a node list with the details of all the elements that match the given xpath
     */
    public static NodeList findAllByXPath(final Document doc, final String extractedXpath) throws XPathExpressionException {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        XPathExpression expr = xpath.compile(XpathUtils.insideTopLevel(extractedXpath));
        return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    }

}
