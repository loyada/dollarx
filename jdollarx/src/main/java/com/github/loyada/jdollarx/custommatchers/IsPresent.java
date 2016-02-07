package com.github.loyada.jdollarx.custommatchers;

import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.PathParsers;
import com.github.loyada.jdollarx.Path;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;

public class IsPresent {

    public IsPresent(){}

    public static Matcher<Path> in(final InBrowser browser) {
        return new TypeSafeMatcher<Path>() {
            private Path el;

            @Override
            public void describeTo(final Description description) {
                description.appendText("browser page contains " + el.toString());
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(CustomMatchersUtil.wrap(el) + " is absent");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el;
                return browser.isPresent(el);
            }
        };
    }


    public static Matcher<Path> in(final Document document) {
        return new TypeSafeMatcher<Path>() {
            private Path el;

            @Override
            public void describeTo(final Description description) {
                description.appendText("document contains " + el.toString());
            }

            @Override
            protected void describeMismatchSafely(final Path el, final
            Description mismatchDescription) {
                mismatchDescription.appendText(CustomMatchersUtil.wrap(el) + " is absent");
            }

            @Override
            protected boolean matchesSafely(final Path el) {
                this.el = el;
                final NodeList nodes;
                try {
                     nodes = PathParsers.findAllByPath(document, el);
                } catch (XPathExpressionException e) {
                    throw new RuntimeException("could not parse");
                }
                return nodes.getLength() > 0;
            }
        };
    }
}
