package com.github.loyada.jdollarx.custommatchers;

import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.PathParsers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;

import static com.github.loyada.jdollarx.ElementProperties.hasText;
import static java.lang.String.format;

/**
 * Internal implementation.
 */
public class HasText {
    private final String text;

    public HasText(String text){
        this.text = text;
    }

    public Matcher<Path> in(final InBrowser browser) {
        return new TypeSafeMatcher<Path>() {
            private Path el;

            @Override
            public String toString() {
                return format("browser page contains the given path with text: %s", text);
            }

            public void describeTo(final Description description) {
                description.appendText("browser page contains " + el.toString());
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(CustomMatchersUtil.wrap(this.el) + " is absent");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el.that(hasText(text));
                return browser.isPresent(this.el);
            }
        };
    }


    public Matcher<Path> in(final Document document) {
        return new TypeSafeMatcher<Path>() {
            private Path el;

            public void describeTo(final Description description) {
                description.appendText("document contains " + el.toString());
            }

            @Override
            public String toString() {
                return format("document contains the given path with text: %s", text);
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(CustomMatchersUtil.wrap(this.el) + " is absent");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el.that(hasText(text));
                final NodeList nodes;
                try {
                    nodes = PathParsers.findAllByPath(document, this.el);
                } catch (XPathExpressionException e) {
                    throw new RuntimeException("could not parse");
                }
                return nodes.getLength() > 0;
            }
        };
    }
}
