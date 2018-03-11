package com.github.loyada.jdollarx.custommatchers;

import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import com.github.loyada.jdollarx.PathParsers;
import com.github.loyada.jdollarx.RelationOperator;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.NoSuchElementException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Optional;

import static com.github.loyada.jdollarx.RelationOperator.opAsEnglish;

/**
 * Internal implementation.
 */
public final class CustomMatchersUtil {


    static public String wrap(Path el) {
        String asString = el.toString();
        return (asString.contains(" ")) ? String.format("(%s)", asString) : asString;
    }

    /**
     * Internal implementation
     */
    public static class NTimesMatcher extends TypeSafeMatcher<Path> {
        private  Path path;
        private final int nTimes;
        private final RelationOperator relationOperator;
        private final InBrowser browser;
        int foundNTimes;

        public NTimesMatcher(final int nTimes, final RelationOperator relationOperator, final InBrowser browser) {
            this.nTimes = nTimes;
            this.relationOperator = relationOperator;
            this.browser = browser;
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText( String.format("browser page contains %s%s%d time%s",
                    CustomMatchersUtil.wrap(path), opAsEnglish(relationOperator), nTimes, nTimes != 1 ? "s" : ""));
        }

        @Override
        protected void describeMismatchSafely(final Path el, final
        Description mismatchDescription) {
            mismatchDescription.appendText(CustomMatchersUtil.wrap(el) + " appears " + foundNTimes + " time" + (foundNTimes!=1 ? "s" : ""));
        }

        @Override
        protected boolean matchesSafely(final Path el) {
            this.path = el;
            try{
                browser.findPageWithNumberOfOccurrences(el, nTimes, relationOperator);
                return true;
            } catch (NoSuchElementException e) {
                foundNTimes = browser.numberOfAppearances(el);
                return false;
            }
        }
    }

    /**
     * Internal implementation
     */
    public static class ISPresentNTimesMatcherForDocument extends TypeSafeMatcher<Path> {
        private  Path path;
        private final int nTimes;
        private final RelationOperator relationOperator;
        private final Document doc;
        int foundNTimes;

        public ISPresentNTimesMatcherForDocument(final int nTimes, final RelationOperator relationOperator, final Document doc) throws ParserConfigurationException, IOException, SAXException {
            this.nTimes = nTimes;
            this.relationOperator = relationOperator;
            this.doc = doc;
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText( String.format("document contains %s%s%d time%s",
                    CustomMatchersUtil.wrap(path), opAsEnglish(relationOperator), nTimes, nTimes != 1 ? "s" : ""));
        }

        @Override
        protected void describeMismatchSafely(final Path el, final
        Description mismatchDescription) {
            mismatchDescription.appendText(CustomMatchersUtil.wrap(el) + " appears " + foundNTimes + " time" + (foundNTimes!=1 ? "s" : ""));
        }

        @Override
        protected boolean matchesSafely(final Path el) {
            this.path = el;
            final NodeList nodes;
            final Optional<String> path = el.getXPath();
            if (!path.isPresent()) {
                throw new UnsupportedOperationException("findPageWithNumberOfOccurrences requires an xpath");
            }
            try {
                nodes = PathParsers.findAllByXPath(doc, path.get());
            } catch (XPathExpressionException e) {
                throw new RuntimeException("could not parse");
            }
            foundNTimes = nodes.getLength();
            switch (relationOperator){
                case exactly: return foundNTimes==nTimes;
                case orMore: return foundNTimes>=nTimes;
                default: return foundNTimes<=nTimes;
            }
        }
    }


}
