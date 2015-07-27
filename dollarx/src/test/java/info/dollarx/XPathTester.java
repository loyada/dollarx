package info.dollarx;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class XPathTester {
    public Document setupHTMLFromString(final String exampleString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream input = new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8));
        return builder.parse(input);
    }

    public NodeList findAllByXpath(final String html, final String path) {
        try {
            Document doc = setupHTMLFromString("<html>" + html + "</html>");
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("//" + path);
            return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (Exception e) {
            throw new AssertionError("could not parse", e);
        }
    }

    public String getText(final Node node) {
        return node.getTextContent();
    }

    public String getCssClass(final Node node) {
        return node.getAttributes().getNamedItem("class").getNodeValue();
    }


    public String getElementName(final Node node) {
        return node.getNodeName();
    }

}
