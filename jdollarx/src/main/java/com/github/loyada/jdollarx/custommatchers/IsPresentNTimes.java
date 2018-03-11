package com.github.loyada.jdollarx.custommatchers;

import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Path;
import org.hamcrest.Matcher;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static com.github.loyada.jdollarx.RelationOperator.*;
import static com.github.loyada.jdollarx.custommatchers.CustomMatchersUtil.ISPresentNTimesMatcherForDocument;
import static com.github.loyada.jdollarx.custommatchers.CustomMatchersUtil.NTimesMatcher;

/**
 * Internal implementation - not to be instantiated directly.
 * This matcher is optimized for the success use-case. In that case it match for a single element
 * with exact number of elements wanted.
 * In case of failure, it will make another call to get the actual number of elements on
 * the page, in order to provide a detailed error message.
 * So the trade off is: In case of success it's faster, In case of failure it's slower. It makes sense since most
 * of the time we expect success.
 */
public class IsPresentNTimes {
    private final int nTimes;

    public IsPresentNTimes(int nTimes){
        this.nTimes = nTimes;
    }



    public Matcher<Path> timesIn(InBrowser browser){
        return new NTimesMatcher(nTimes, exactly, browser);
    }

    public Matcher<Path> timesOrMoreIn(InBrowser browser){
        return new NTimesMatcher(nTimes, orMore, browser);
    }

    public Matcher<Path> timesOrLessIn(InBrowser browser){
        return new NTimesMatcher(nTimes, orLess, browser);
    }

    public Matcher<Path> timesIn(Document doc) throws IOException, SAXException, ParserConfigurationException {
        return new ISPresentNTimesMatcherForDocument(nTimes, exactly, doc);
    }

    public Matcher<Path> timesOrMoreIn(Document doc) throws IOException, SAXException, ParserConfigurationException {
        return new ISPresentNTimesMatcherForDocument(nTimes, orMore, doc);
    }

    public Matcher<Path> timesOrLessIn(Document doc) throws IOException, SAXException, ParserConfigurationException {
        return new ISPresentNTimesMatcherForDocument(nTimes, orLess, doc);
    }
}
