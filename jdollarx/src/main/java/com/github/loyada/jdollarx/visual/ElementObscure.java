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

public class ElementObscure implements AutoCloseable {
    private final Map<WebElement, String> styleByElement = new HashMap<>();
    final boolean strict;
    final JavascriptExecutor js;
    final List<Path> obscuredElements = new ArrayList<>();


    public ElementObscure(InBrowser browser, Path element) {
        this(browser, List.of(element), false);
    }

    public ElementObscure(InBrowser browser, List<Path> elements) {
        this(browser, elements, false);
    }

    public ElementObscure(InBrowser browser, List<Path> elements, boolean strict) {
        this.strict = strict;
        js = (JavascriptExecutor) browser.getDriver();

        elements.forEach(el -> {
            final WebElement webEl;
            try {
                webEl = browser.find(el);
            } catch (NoSuchElementException e) {
                if (strict) {
                    throw e;
                }
                else return;
            }

            obscuredElements.add(el);
            String oldStyle = webEl.getAttribute("style");
            styleByElement.put(webEl, oldStyle);

            js.executeScript("arguments[0].setAttribute('style', arguments[1] + ' display: none;');",
                    webEl, Optional.ofNullable(oldStyle).orElse(""));
        });
    }

    public List<Path> getObscuredElements() {
        return ImmutableList.copyOf(obscuredElements);
    }

    @Override
    public void close() throws Exception {
        styleByElement.keySet().forEach(webEl -> {
            String script = format("arguments[0].setAttribute('style', '%s');", styleByElement.get(webEl));
            js.executeScript(script, webEl);
        });
    }
}