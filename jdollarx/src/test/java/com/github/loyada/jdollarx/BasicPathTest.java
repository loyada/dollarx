package com.github.loyada.jdollarx;


import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.w3c.dom.NodeList;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.mock;

import static com.github.loyada.jdollarx.ElementProperties.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static com.github.loyada.jdollarx.BasicPath.*;

public class BasicPathTest extends XPathTester {


    @Test
    public void divAfterSpan() {
        Path el = div.after(span);
        NodeList nodes = findAllByXpath("<div>foo</div><span></span>><div>bar</div>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("bar"));
        assertThat(el.toString(), is(equalTo("div, after span")));
    }

    @Test
    public void customElementTest() {
        Path el = BasicPath.customElement("div").after(span);
        NodeList nodes = findAllByXpath("<div>foo</div><span></span>><div>bar</div>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("bar"));
        assertThat(el.toString(), is(equalTo("div, after span")));
    }

    @Test
    public void divBeforeSpan() {
        Path el = div.before(span);
        NodeList nodes = findAllByXpath("<div>foo</div><span></span>><div>boo</div>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("foo"));
        assertThat(el.toString(), is(equalTo("div, before span")));
    }

    @Test
    public void isAfterSiblingTest() {
        Path el = element.afterSibling(div.withClass("a"));
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/><span class='c'/></div><div>c</div><div>d</div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("b"));
        assertThat(getCssClass(nodes.item(1)), equalTo("c"));
        assertThat(el.toString(), is(equalTo("any element, after the sibling (div, that has class a)")));
    }

    @Test
    public void isImmediatelyAfterSiblingTest() {
        Path el = element.immediatelyAfterSibling(div.withClass("a"));
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/><span class='c'/></div><div>c</div><div>d</div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("b"));
        assertThat(el.toString(), is(equalTo("any element, immediately after the sibling (div, that has class a)")));
    }

    @Test
    public void textNodeTest() {
        Path el = element.after(textNode("abc"));
        NodeList nodes = findAllByXpath("<span>aaa</span><span>abc<span>1</span></span><span>2</span>", el);
        assertThat(nodes.getLength(), is(2));
        assertThat(getText(nodes.item(0)), equalTo("1"));
        assertThat(getText(nodes.item(1)), equalTo("2"));

    }

    @Test
    public void insideTopLevelTest() {
        Path el = div.before(span).insideTopLevel();

        NodeList nodes = findAllByXpath("<div>foo</div><span></span>><div>boo</div>", el);

        assertThat(el.getXPath().get(), equalTo("//span/preceding::div"));
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("foo"));
        assertThat(el.toString(), is(equalTo("div, before span")));
    }

    @Test
    public void insideTopLevelMultipleTest() {
        //note that applying insideTopLevel() multiple times should not do anything
        Path el = div.before(span).insideTopLevel().insideTopLevel();

        NodeList nodes = findAllByXpath("<div>foo</div><span></span>><div>boo</div>", el);

        assertThat(el.getXPath().get(), equalTo("//span/preceding::div"));
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("foo"));
        assertThat(el.toString(), is(equalTo("div, before span")));
    }

    @Test
    public void insideTopLevelVariationTest() {
        // Note that withGlobalIndex implies inside top level, so insideTopLevel should not add anything
        Path el = div.before(span).withGlobalIndex(2).insideTopLevel();

        NodeList nodes = findAllByXpath("<span/><div>foo</div><span></span><div>boo</div><span/><div class='3'/><span/><div/>", el);

        assertThat(el.getXPath().get(), equalTo("(//span/preceding::div)[3]"));
        assertThat(nodes.getLength(), is(1));
        assertThat(getElementName(nodes.item(0)), equalTo("div"));
        assertThat(getCssClass(nodes.item(0)), equalTo("3"));
        assertThat(el.toString(), is(equalTo("occurrence number 3 of (div, before span)")));
    }

    @Test
    public void  nestedIndexTest() {
        Path aDiv = div.withClass("a").withGlobalIndex(1);
        Path el = (span.inside(aDiv)).withGlobalIndex(1);
        NodeList nodes = findAllByXpath("<div class='a'>foo" +
                "                            <span>1</span>" +
                "                            <span>2</span>" +
                "                         </div>" +
                "                         <div class='a'>" +
                "                            <span>5" +
                "                                <span>3</span>" +
                "                                <span>4</span>" +
                "                            </span>" +
                "                            <span>6</span>" +
                "                         </div>", el);
        assertThat(nodes.getLength(), equalTo(1));
        assertThat(getText(nodes.item(0)), equalTo("3"));
        assertThat(el.toString(), equalTo("occurrence number 2 of (span, inside (occurrence number 2 of (div, that has class a)))"));
    }

    @Test
    public void isBeforeSiblingTest() {
        Path el = element.beforeSibling(span.withClass("abc"));
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(4));
        assertThat(el.toString(), is(equalTo("any element, before the sibling (span, that has class abc)")));
    }

    @Test
    public void isImmediatelyBeforeSiblingTest() {
        Path el = span.immediatelyBeforeSibling(span.withClass("c"));
        NodeList nodes = findAllByXpath("<div></div><span class='c'/><div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/><span class='c'/></div><div>c</div><div>d</div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("b"));
        assertThat(el.toString(), is(equalTo("span, immediately before the sibling (span, that has class c)")));
    }

    @Test
    public void isChildTest() {
        Path el = element.that(isChildOf(div));
        NodeList nodes = findAllByXpath("<div>a</div><div><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(3));
        assertThat(getCssClass(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a.a"));
        assertThat(getCssClass(nodes.item(2)), equalTo("b"));
        assertThat(el.toString(), is(equalTo("any element, that is child of: div")));
    }

    @Test
    public void hasParentTest() {
        Path el = element.childOf(div);
        NodeList nodes = findAllByXpath("<div>a</div><div><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(3));
        assertThat(getCssClass(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a.a"));
        assertThat(getCssClass(nodes.item(2)), equalTo("b"));
        assertThat(el.toString(), is(equalTo("any element, child of div")));
    }

    @Test
    public void isParentTest() {
        Path el = element.that(hasClass("container").or(hasClass("a"))).inside(BasicPath.body.withClass("bar")).inside(BasicPath.html).parentOf(div);
        NodeList nodes = findAllByXpath("<body class=\"bar\"><div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span></body>", el);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("any element, (has class container or has class a), inside (document body, that has class bar), inside document, parent of div")));
    }

    @Test
    public void hasChildTest() {
        Path el = element.inside(BasicPath.html).that(hasChild(div));
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("any element, inside document, and has child: div")));
    }

    @Test
    public void isDescendantTest() {
        Path el = div.descendantOf(div.withClass("container"));
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a.a"));
        assertThat(el.toString(), is(equalTo("div, descendant of div, that has class container")));
    }

    @Test
    public void isInsideTest() {
        Path el = div.inside(div.withClass("container"));
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a.a"));
        assertThat(el.toString(), is(equalTo("div, inside (div, that has class container)")));
    }

    @Test
    public void isAncestorOfTest() {
        Path el = div.ancestorOf(div.withClass("a.a"));
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("div, ancestor of div, that has class a.a")));
    }

    @Test
    public void containingTest() {
        Path el = div.containing(div.withClass("a.a"));
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("div, ancestor of div, that has class a.a")));
    }

    @Test
    public void containsTest() {
        Path el = div.contains(div.withClass("a.a"));
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("div, ancestor of div, that has class a.a")));
    }

    @Test
    public void indexTest() {
        Path el = div.withGlobalIndex(1);
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(el.toString(), is(equalTo("occurrence number 2 of div")));
    }

    @Test
    public void indexVariationTest() {
        Path el = div.withGlobalIndex(1);
        NodeList nodes = findAllByXpath("<div>a<div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span></div>", el);
        assertThat(nodes.getLength(), equalTo(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(el.toString(), equalTo("occurrence number 2 of div"));
    }

    @Test
    public void indexAndInsideNegativeTest() {
        Path el = div.withGlobalIndex(0).inside(span);
        NodeList nodes = findAllByXpath("<div>foo</div><span><div>bar</div></span>", el);
        assertThat(nodes.getLength(), is(0));
        assertThat(el.toString(), equalTo("the first occurrence of div, and is inside span"));
    }

    @Test
    public void indexAndInsidePositiveTest() {
        Path el = div.withGlobalIndex(1).inside(span);
        NodeList nodes = findAllByXpath("<div>foo</div><span><div>bar</div></span>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(el.toString(), equalTo("occurrence number 2 of div, and is inside span"));
    }

    @Test
    public void insideAndIndexTest() {
        Path el = div.inside(span).withGlobalIndex(1);
        NodeList nodes = findAllByXpath("<div>foo</div><span><div class='foo'>bar</div></span><span><div class='bar'>bar</div></span>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("bar"));
        assertThat(el.toString(), equalTo("occurrence number 2 of (div, inside span)"));
    }

    @Test
    public void childNumberTest() {
        Path el = childNumber(1).ofType(span.withClass("a"));
        NodeList nodes = findAllByXpath("<span></span><span class='a x'>a<span class='a y'>b</span><span class='a z'>c</span></span>", el);
        assertThat(nodes.getLength(), equalTo(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("a x"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a y"));
        assertThat(el.toString(), equalTo("child number 1 of type(span, that has class a)"));
    }

    @Test
    public void occurrenceNumberTest() {
        Path el = occurrenceNumber(2).of(div);
        NodeList nodes = findAllByXpath("<div>a<div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span></div>", el);
        assertThat(nodes.getLength(), equalTo(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(el.toString(), equalTo("occurrence number 2 of div"));
    }

    @Test
    public void withTextTest() {
        Path el = div.withText("AB");
        NodeList nodes = findAllByXpath("<div>ab</div><div>abc</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("ab"));
        assertThat(el.toString(), is(equalTo("div, that has the text \"AB\"")));
    }

    @Test
    public void withClassesTest() {
        Path el = div.withClasses("a", "b");
        NodeList nodes = findAllByXpath("<div class=\"a\">a</div><div class=\"b\">b</div><div class=\"a b\">a b</div>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("a b"));
        assertThat(el.toString(), is(equalTo("div, that has classes [a, b]")));
    }

    @Test
    public void withTextContainingTest() {
        Path el = div.withTextContaining("AB");
        NodeList nodes = findAllByXpath("<div>ab</div><div>xabc</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(2));
        assertThat(getText(nodes.item(0)), equalTo("ab"));
        assertThat(getText(nodes.item(1)), equalTo("xabc"));
        assertThat(el.toString(), is(equalTo("div, that has text containing \"AB\"")));
    }

    @Test
    public void andTest() {
        Path el = div.that(hasClass("a")).and(hasText("xyz"));
        NodeList nodes = findAllByXpath("<div>ab</div><div>xabc</div><div class='container'><div class='a dfdsf'>xyz<div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("xyz"));
        assertThat(getCssClass(nodes.item(0)), equalTo("a dfdsf"));
        assertThat(el.toString(), is(equalTo("div, that has class a, and has the text \"xyz\"")));
    }

    @Test
    public void withDescriptionAndAdditionalProperty() {
        Path el = div.ancestorOf(div.withClass("foo").describedBy("abc").withClass("a.a"));
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='foo a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("div, ancestor of abc, that has class a.a")));
    }

    @Test
    public void firstOccurrenceTest() {
        Path el = BasicPath.firstOccurrenceOf(element.withClass("a"));
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a first'><div class='foo a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("a first"));
        assertThat(el.toString(), is(equalTo("the first occurrence of (any element, that has class a)")));
    }

    @Test
    public void lastOccurrenceTest() {
        Path el = lastOccurrenceOf(element.withClass("a"));
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a first'><div class='foo a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='last a'></span>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("last a"));
        assertThat(el.toString(), is(equalTo("the last occurrence of (any element, that has class a)")));
    }

    @Test
    public void withDescriptionAndAdditionalProperty2() {
        Path el = div.ancestorOf(div.that(hasClass("foo")).describedBy("abc").that(hasClass("a.a")));
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='foo a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("div, ancestor of abc, that has class a.a")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalOperationTest() {
        div.or(new PathBuilder().build());
    }

    @Test
    public void bareboneTostring() {
        BasicPath path = new PathBuilder().withXpath("Johhny").build();
        assertThat(path.toString(), is(equalTo("xpath: \"Johhny\"")));
    }

    @Test
    public void underlyingToString() {
        BasicPath path = new PathBuilder().withXpath("Johhny").withUnderlying(mock(WebElement.class)).build();
        assertThat(path.toString(), startsWith("under reference element Mock for WebElement"));
        assertThat(path.toString(), endsWith("xpath: \"Johhny\""));
    }

    @Test
    public void ToStringWith2ElementProperties() {
        Path path = anchor.that(hasClass("foo"), hasClass("bar"));
        assertThat(path.toString(), endsWith("anchor, that has class foo, and has class bar"));
    }

    @Test
    public void ToStringWithDescription() {
        Path path = anchor.withClass("x").describedBy("foo");
        assertThat(path.toString(), endsWith("foo"));
    }

    @Test
    public void orWithImmediatelyAfter() {
        Path el = div.immediatelyAfterSibling(span).or(input.after(label));
        assertThat(el.getXPath().get(), equalTo("*[(self::div[preceding-sibling::*[1]/self::span]) | (self::input[preceding::label])]"));
        NodeList nodes = findAllByXpath("<div>a<span class='container'>aaa</span><div>bbb</div></div><span></span><a></a><div>ccc</div><label></label><input></input>", el);
        assertThat(nodes.getLength(), equalTo(2));
        assertThat(getText(nodes.item(0)), equalTo("bbb"));
        assertThat(getElementName(nodes.item(1)), equalTo("input"));
        assertThat(el.toString(), endsWith("(div, immediately after the sibling span) or (input, after label)"));
    }

    @Test
    public void notDivBeforeSpanIsInvalid() {
        Path path = PathOperators.not(div.before(span));
        logit(path);
        assertThat(path.getXPath().get(), equalTo("*[not(self::div[following::span])]"));
    }

    @Test
    public void notDivWithClass() {
        Path el = PathOperators.not(div.withClass("foo"));
        NodeList nodes = findAllByXpath("<div class='foo'>foo</div><span></span>><div>boo</div>", el);
        assertThat(nodes.getLength(), is(3));  // this includes the artificial "HTML" that we add in findAllByXpath()
        assertThat(getText(nodes.item(2)), equalTo("boo"));
        assertThat(getElementName(nodes.item(1)), equalTo("span"));
        assertThat(getElementName(nodes.item(0)), equalTo("html"));
        assertThat(el.toString(), is(equalTo("anything except (div, that has class foo)")));

    }

    @Test
    public void notDivInsideSpan() {
        Path el = PathOperators.not(div).inside(span);
        NodeList nodes = findAllByXpath("<span><div class='foo' />foo<a></a></span><span></span>><div>boo</div>", el);
        assertThat(nodes.getLength(), is(1));
        assertThat(getElementName(nodes.item(0)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("anything except (div), inside span")));
    }

    @Test
    public void notDivInsideSpanRelationIsInvalid() {
        Path path = PathOperators.not(div.inside(span));
        assertThat(path.getXPath().get(), equalTo("*[not(self::div[ancestor::span])]"));
    }

}
