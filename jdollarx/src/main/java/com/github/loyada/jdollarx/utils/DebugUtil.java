package com.github.loyada.jdollarx.utils;

import com.github.loyada.jdollarx.Path;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.driver;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.findAll;

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
     * This method relies on {@link com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton}, and rely on the library JSoup.
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
}
