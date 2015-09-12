package info.testtools.jdollarx;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static info.testtools.jdollarx.ElementProperties.*;

public class BasicPathCreationTest {

    @Test
    public void divBeforeSpan(){
        BasicPath el = BasicPath.div.before(BasicPath.span);
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("span/preceding::div")));
        assertThat(el.toString(), is(equalTo("div, before span")));
    }

    @Test
    public void divAfterSpan(){
        BasicPath el = BasicPath.div.after(BasicPath.span);
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("span/following::div")));
        assertThat(el.toString(), is(equalTo("div, after span")));
    }

    @Test
    public void divBeforeSiblingSpan(){
        BasicPath el = BasicPath.div.beforeSibling(BasicPath.span);
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("span/preceding-sibling::div")));
        assertThat(el.toString(), is(equalTo("div, before the sibling span")));

    }

    @Test
      public void complexBeforeAndAfter(){
        // this is a really complicated way of saying div.before(span), for the sake of testing
        Path el = BasicPath.div.that(isBefore(BasicPath.span)).before(BasicPath.span.that(isAfter(BasicPath.div.that(isBefore(BasicPath.span)))));
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("span[preceding::div[following::span]]/preceding::div[following::span]")));
        assertThat(el.toString(), is(equalTo("div, that is before: span, before (span, that is after: (div, that is before: span))")));
    }

    @Test
    public void withClassAndIndex(){
        Path el = BasicPath.div.that(hasClass("foo")).withIndex(5);
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("div[contains(concat(' ', normalize-space(@class), ' '), ' foo ')][6]")));
        assertThat(el.toString(), is(equalTo("div, that has class foo, and with the index 5")));
    }

    @Test
    public void withClassAndInside(){
        Path dialog = BasicPath.div.withClass("ui-dialog");
        Path el = BasicPath.span.that(hasClass("foo"), isInside(dialog));
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("span[contains(concat(' ', normalize-space(@class), ' '), ' foo ')][ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' ui-dialog ')]]")));
        assertThat(el.toString(), is(equalTo("span, that has class foo, and has ancestor: (div, that has class ui-dialog)")));
    }

    @Test
    public void withClassAndInsidedialogWithDescription(){
        Path dialog = BasicPath.div.withClass("ui-dialog").describedBy("sumbission form");
        Path el = BasicPath.span.that(hasClass("foo"), isInside(dialog));
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("span[contains(concat(' ', normalize-space(@class), ' '), ' foo ')][ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' ui-dialog ')]]")));
        assertThat(el.toString(), is(equalTo("span, that has class foo, and has ancestor: (sumbission form)")));
    }

    @Test
    public void divOrSpan(){
        Path el = BasicPath.div.or(BasicPath.span);
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("*[self::div | self::span]")));
        assertThat(el.toString(), is(equalTo("div or span")));
    }

    @Test
    public void divWithClassOrSpan(){
        Path el = BasicPath.div.withClass("foo").or(BasicPath.span);
        String xpath = el.getXPath().get();
        assertThat(xpath, is(equalTo("*[self::div[contains(concat(' ', normalize-space(@class), ' '), ' foo ')] | self::span]")));
        assertThat(el.toString(), is(equalTo("(div, that has class foo) or span")));
    }

}
