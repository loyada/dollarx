package info.testtools.jdollarx;


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

public final class PathParsers {
    private PathParsers(){}

    public static Document getDocumentFromString(final String exampleString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream input = new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8));
        return builder.parse(input);
    }

    public static NodeList findAllByPath(final String docString, final Path path) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
        return findAllByPath(getDocumentFromString(docString), path );
    }

    public static NodeList findAllByPath(final Document doc, final Path path) throws XPathExpressionException {
       return findAllByXPath(doc, path.getXPath().get());
    }

    public static NodeList findAllByXPath(final Document doc, final String extractedXpath) throws XPathExpressionException {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        final String prefix =  (extractedXpath.startsWith("/") || extractedXpath.startsWith("(/")) ? "" :
                (extractedXpath.startsWith("(")) ? "(//" :
                        "//";
        final int chopn = (extractedXpath.startsWith("(") && !extractedXpath.startsWith("(/")) ? 1 : 0;
        XPathExpression expr = xpath.compile(prefix + extractedXpath.substring(chopn));
        return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    }

}
