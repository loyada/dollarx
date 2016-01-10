package info.testtools.jdollarx;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XPathTester {
    public NodeList findAllByXpath(final String html, Path path){
        try {
            return PathParsers.findAllByPath("<html>" + html + "</html>", path);
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
