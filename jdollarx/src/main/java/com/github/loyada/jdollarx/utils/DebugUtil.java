package com.github.loyada.jdollarx.utils;

import com.github.loyada.jdollarx.Path;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.find;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.findAll;
import static java.lang.String.format;

/**
 * Several utilities that are useful for troubleshooting of existing browser pages.
 * The utilities assume the use of {@link com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton}.
 */
public final class DebugUtil {

    private DebugUtil() {
    }

    /**
     * Get all matches of the path as a list of {@link Element}.
     * JSoup {@link Element} are a nice, readable way to examine DOM objects.
     * This is useful for troubleshooting.
     * This method relies on {@link com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton}, and on the library JSoup.
     *
     * @param el the path we are looking for
     * @return all the elements that match it in the current page
     */
    public static List<Element> getDOMOfAll(final Path el) {
        List <WebElement> els = findAll(el);
        return els.stream().map(we -> we.getAttribute("outerHTML")).
                        map(outerHTML -> Jsoup.parseBodyFragment(outerHTML).body().child(0)).
                        collect(Collectors.toList());
    }

    /**
     * Same as {@link #getDOMOfAll(Path)}, but returns an optional of the first match.
     * @param el the path we are looking for
     * @return the first Element that matches the path in the current page
     */
    public static Optional<Element> getDOM(final Path el) {
        return getDOMOfAll(el).stream().findFirst();
    }

    /**
     * Highlight the first element that match the path in the browser, for 2 seconds.
     * @param el - the definition of the element to highlight
     */
    public static void highlight(final Path el) {
        try {
            WebElement webEl = find(el);
            highlight_webel_list_internal(Arrays.asList(webEl));
        } finally {};
    }

    /**
     * Highlight all the elements that match the path in the browser, for 2 seconds.
     * @param el - the definition of the elements to highlight
     */
    public static void highlightAll(final Path el) {
        List <WebElement> els = findAll(el);
        highlight_webel_list_internal(els);
    }

    private static void highlight_webel_list_internal(List<WebElement> els){
        List<String> oldStyles = els.stream().map(e -> highlight_internal(e)).collect(Collectors.toList());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        JavascriptExecutor js = (JavascriptExecutor) driver;
        IntStream.range(0, els.size()).forEach(ind ->
            js.executeScript(format("arguments[0].setAttribute('style', '%s');", oldStyles.get(ind))
                , els.get(ind)));
    }

    private static String highlight_internal(WebElement webEl) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String oldStyle = webEl.getAttribute("style");

        js.executeScript("arguments[0].setAttribute('style', arguments[1] + ' background: yellow; border: 4px solid red;');",
                webEl, Optional.ofNullable(oldStyle).orElse(""));
        return oldStyle;
    }
    /**
     * Download the current page and convert it to a W3C Document, which can be
     * inspected using the {@link com.github.loyada.jdollarx.PathParsers} methods
     *
     * @return a W3C document
     */
    public static  org.w3c.dom.Document getPageAsW3CDoc() {
        String html = (String)((JavascriptExecutor) driver).executeScript("return document.documentElement.outerHTML");
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        return DOMBuilder.jsoup2DOM(jsoupDoc);
    }

    /**
     *
     * @author <a href="mailto:kasper@dfki.de">Walter Kasper</a>
     *
     */
    private static class DOMBuilder {

        /**
         * Restrict instantiation
         */
        private DOMBuilder() {}

        /**
         * Returns a W3C DOM that exposes the same content as the supplied Jsoup document into a W3C DOM.
         * @param jsoupDocument The Jsoup document to convert.
         * @return A W3C Document.
         */
        public static Document jsoup2DOM(org.jsoup.nodes.Document jsoupDocument) {

            Document document = null;

            try {

      /* Obtain the document builder for the configured XML parser. */
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

      /* Create a document to contain the content. */
                document = docBuilder.newDocument();
                createDOM(jsoupDocument, document, document, new HashMap<String,String>());

            } catch (ParserConfigurationException pce) {
                throw new RuntimeException(pce);
            }

            return document;
        }

        /**
         * The internal helper that copies content from the specified Jsoup <tt>Node</tt> into a W3C {@link Node}.
         * @param node The Jsoup node containing the content to copy to the specified W3C {@link Node}.
         * @param out The W3C {@link Node} that receives the DOM content.
         */
        private static void createDOM(org.jsoup.nodes.Node node, Node out, Document doc, Map<String,String> ns) {

            if (node instanceof org.jsoup.nodes.Document) {

                org.jsoup.nodes.Document d = ((org.jsoup.nodes.Document) node);
                for (org.jsoup.nodes.Node n : d.childNodes()) {
                    createDOM(n, out,doc,ns);
                }

            } else if (node instanceof org.jsoup.nodes.Element) {

                org.jsoup.nodes.Element e = ((org.jsoup.nodes.Element) node);
                org.w3c.dom.Element _e = doc.createElement(e.tagName());
                out.appendChild(_e);
                org.jsoup.nodes.Attributes atts = e.attributes();

                for(org.jsoup.nodes.Attribute a : atts){
                    String attName = a.getKey();
                    //omit xhtml namespace
                    if (attName.equals("xmlns")) {
                        continue;
                    }
                    String attPrefix = getNSPrefix(attName);
                    if (attPrefix != null) {
                        if (attPrefix.equals("xmlns")) {
                            ns.put(getLocalName(attName), a.getValue());
                        }
                        else if (!attPrefix.equals("xml")) {
                            String namespace = ns.get(attPrefix);
                            if (namespace == null) {
                                //fix attribute names looking like qnames
                                attName = attName.replace(':','_');
                            }
                        }
                    }
                    _e.setAttribute(attName, a.getValue());
                }

                for (org.jsoup.nodes.Node n : e.childNodes()) {
                    createDOM(n, _e, doc,ns);
                }

            } else if (node instanceof org.jsoup.nodes.TextNode) {

                org.jsoup.nodes.TextNode t = ((org.jsoup.nodes.TextNode) node);
                if (!(out instanceof Document)) {
                    out.appendChild(doc.createTextNode(t.text()));
                }
            }
        }

        // some hacks for handling namespace in jsoup2DOM conversion
        private static String getNSPrefix(String name) {
            if (name != null) {
                int pos = name.indexOf(':');
                if (pos > 0) {
                    return name.substring(0,pos);
                }
            }
            return null;
        }

        private static String getLocalName(String name) {
            if (name != null) {
                int pos = name.lastIndexOf(':');
                if (pos > 0) {
                    return name.substring(pos+1);
                }
            }
            return name;
        }

    }
}
