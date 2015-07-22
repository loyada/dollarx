package com.lab49.jdollarx;

import org.junit.Test;
import org.lab49.jdollarx.BasicPath;
import org.lab49.jdollarx.Path;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.lab49.jdollarx.BasicPath.*;
import static org.lab49.jdollarx.ElementProperties.*;

public class BasicPathCreationTest {

    @Test
    public void divBeforeSpan(){
        BasicPath el = div.before(span);
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("span/preceding::div")));
        assertThat(el.toString(), is(equalTo("div, before span")));
    }

    @Test
    public void divAfterSpan(){
        BasicPath el = div.after(span);
        el.that(hasText("John"));
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("span/following::div")));
        assertThat(el.toString(), is(equalTo("div, after span")));
    }

    @Test
    public void divBeforeSiblingSpan(){
        BasicPath el = div.beforeSibling(span);
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("span/preceding-sibling::div")));
        assertThat(el.toString(), is(equalTo("div, before the sibling span")));

    }

    @Test
      public void complexBeforeAndAfter(){
        // this is a really complicated way of saying div.before(span), for the sake of testing
        Path el = div.that(isBefore(span)).before(span.that(isAfter(div.that(isBefore(span)))));
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("span[preceding::div[following::span]]/preceding::div[following::span]")));
        assertThat(el.toString(), is(equalTo("div, that is before: span, before (span, that is after: (div, that is before: span))")));
    }

    @Test
    public void withClassAndIndex(){
        Path el = div.that(hasClass("foo")).withIndex(5);
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("div[contains(concat(' ', @class, ' '), ' foo ')][6]")));
        assertThat(el.toString(), is(equalTo("div, that has class foo, and with the index 5")));
    }

    @Test
    public void withClassAndInside(){
        Path dialog = div.withClass("ui-dialog");
        Path el = span.that(hasClass("foo"), isInside(dialog));
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("span[contains(concat(' ', @class, ' '), ' foo ')][ancestor::div[contains(concat(' ', @class, ' '), ' ui-dialog ')]]")));
        assertThat(el.toString(), is(equalTo("span, that has class foo, and has ancestor: (div, that has class ui-dialog)")));
    }

    @Test
    public void withClassAndInsidedialogWithDescription(){
        Path dialog = div.withClass("ui-dialog").describedBy("sumbission form");
        Path el = span.that(hasClass("foo"), isInside(dialog));
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("span[contains(concat(' ', @class, ' '), ' foo ')][ancestor::div[contains(concat(' ', @class, ' '), ' ui-dialog ')]]")));
        assertThat(el.toString(), is(equalTo("span, that has class foo, and has ancestor: (sumbission form)")));
    }

    @Test
    public void divOrSpan(){
        Path el = div.or(span);
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("*[self::div | self::span]")));
        assertThat(el.toString(), is(equalTo("div or span")));
    }

    @Test
    public void divWithClassOrSpan(){
        Path el = div.withClass("foo").or(span);
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("*[self::div[contains(concat(' ', @class, ' '), ' foo ')] | self::span]")));
        assertThat(el.toString(), is(equalTo("(div, that has class foo) or span")));
    }

}
