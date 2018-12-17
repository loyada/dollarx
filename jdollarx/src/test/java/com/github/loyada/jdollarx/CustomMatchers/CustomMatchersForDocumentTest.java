package com.github.loyada.jdollarx.CustomMatchers;

import com.github.loyada.jdollarx.PathParsers;
import com.github.loyada.jdollarx.custommatchers.CustomMatchers;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static com.github.loyada.jdollarx.BasicPath.div;
import static com.github.loyada.jdollarx.BasicPath.span;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class CustomMatchersForDocumentTest {

    @Test
    public void isPresentSuccess() throws IOException, SAXException, ParserConfigurationException {
        Document doc = PathParsers.getDocumentFromString("<div><span></span></div>");
        assertThat(span.inside(div), CustomMatchers.isPresentIn(doc));
    }

    @Test
    public void isPresentFailure() throws IOException, SAXException, ParserConfigurationException {
        Document doc = PathParsers.getDocumentFromString("<div><span></span></div>");
        try {
            assertThat(div.inside(span), CustomMatchers.isPresentIn(doc));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: document contains div, inside span\n     but: div, inside span is absent")));
        }
    }

    @Test
    public void isAbsentSuccess() throws IOException, SAXException, ParserConfigurationException {
        Document doc = PathParsers.getDocumentFromString("<div><span></span></div>");
        assertThat(div.inside(span), CustomMatchers.isAbsentFrom(doc));
    }

    @Test
    public void isAbsentFailure() throws IOException, SAXException, ParserConfigurationException {
        Document doc = PathParsers.getDocumentFromString("<div><span></span></div>");
        try {
            assertThat(span.inside(div), CustomMatchers.isAbsentFrom(doc));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: document does not contain span, inside div\n     but: span, inside div is present")));
        }
    }

    @Test
    public void isPresentNTimesVariationFailed() throws IOException, SAXException, ParserConfigurationException {
        Document doc = PathParsers.getDocumentFromString("<div><span><div/></span></div>");
        try {
            assertThat(div, CustomMatchers.isPresent(5).timesIn(doc));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: document contains div 5 times\n     but: div appears 2 times")));
        }
    }

    @Test
    public void isPresentNTimesVariationSuccess() throws IOException, SAXException, ParserConfigurationException {
        Document doc = PathParsers.getDocumentFromString("<div><span><div></div></span></div>");
        assertThat(div, CustomMatchers.isPresent(2).timesIn(doc));
    }

    @Test
    public void isPresentNTimesOrMoreFailed() throws IOException, SAXException, ParserConfigurationException {
        Document doc = PathParsers.getDocumentFromString("<div><span><div/></span></div>");
        try {
            assertThat(div, CustomMatchers.isPresent(5).timesOrMoreIn(doc));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: document contains div at least 5 times\n     but: div appears 2 times")));
        }
    }

    @Test
    public void isPresentNTimesOrMoreSuccess() throws IOException, SAXException, ParserConfigurationException {
        Document doc = PathParsers.getDocumentFromString("<div><span><div/></span></div>");
        assertThat(div, CustomMatchers.isPresent(2).timesOrMoreIn(doc));
    }


    @Test
    public void isPresentNTimesOrLessFailed() throws IOException, SAXException, ParserConfigurationException {
        Document doc = PathParsers.getDocumentFromString("<div><span><div/></span></div>");
        try {
            assertThat(div, CustomMatchers.isPresent(1).timesOrLessIn(doc));
            fail("should fail");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("\nExpected: document contains div at most 1 time\n     but: div appears 2 times")));
        }
    }

    @Test
    public void isPresentNTimesOrLessSuccess() throws IOException, SAXException, ParserConfigurationException {
        Document doc = PathParsers.getDocumentFromString("<div><span><div/></span></div>");
        assertThat(div, CustomMatchers.isPresent(5).timesOrLessIn(doc));
    }
}
