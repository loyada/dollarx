package com.github.loyada.jdollarx.visual;

import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import com.google.common.collect.ImmutableList;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

/**
 * An AutoCloseable that allows to make a list of given elements temporarily
 * invisible, so that the image can be captured/asserted while ignoring certain DOM
 * elements.
 * When closing, the DOM is reversed to the original state.
 * The difference between ElementObsceure and AllElementObscure is that ElementObscure obscures the first
 * match for each path, and AllElementsObscure obscures all the matches. Also, AllElementsObscure does not
 * throw if there are no matches.
 */
public class AllElementsObscure implements AutoCloseable {
    private final Map<WebElement, String> styleByElement = new HashMap<>();
    final boolean strict;
    final JavascriptExecutor js;
    final List<Path> obscuredElements = new ArrayList<>();


    public AllElementsObscure(InBrowser browser, Path element) {
        this(browser, List.of(element), false);
    }

    public AllElementsObscure(InBrowser browser, List<Path> elements) {
        this(browser, elements, false);
    }

    public AllElementsObscure(InBrowser browser, List<Path> elements, boolean strict) {
        this.strict = strict;
        js = (JavascriptExecutor) browser.getDriver();

        elements.forEach(el -> {
            final List<WebElement> webEls;
            webEls = browser.findAll(el);
            if (!webEls.isEmpty()) {
                obscuredElements.add(el);
                webEls.forEach(webEl -> {
                    String oldStyle = webEl.getAttribute("style");
                    styleByElement.put(webEl, oldStyle);
                    js.executeScript("arguments[0].setAttribute('style', arguments[1] + ' display: none;');",
                            webEl, Optional.ofNullable(oldStyle).orElse(""));
                });
            }
        });
    }

    public List<Path> getObscuredElements() {
        return ImmutableList.copyOf(obscuredElements);
    }

    public int getNumberOfElementsObscured() {
        return styleByElement.size();
    }

    @Override
    public void close() throws Exception {
        styleByElement.keySet().forEach(webEl -> {
            String script = format("arguments[0].setAttribute('style', '%s');", styleByElement.get(webEl));
            js.executeScript(script, webEl);
        });
    }
}
