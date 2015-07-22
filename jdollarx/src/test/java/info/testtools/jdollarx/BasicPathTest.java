package info.testtools.jdollarx;


import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.w3c.dom.NodeList;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.mock;

import static info.testtools.jdollarx.ElementProperties.*;
import static org.junit.Assert.assertThat;

public class BasicPathTest extends XPathTester {

    @Test
    public void divAfterSpan() {
        BasicPath el = BasicPath.div.after(BasicPath.span);
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>foo</div><span></span>><div>bar</div>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("bar"));
        assertThat(el.toString(), is(equalTo("div, after span")));
    }

    @Test
    public void divBeforeSpan() {
        BasicPath el = BasicPath.div.before(BasicPath.span);
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>foo</div><span></span>><div>boo</div>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("foo"));
        assertThat(el.toString(), is(equalTo("div, before span")));
    }

    @Test
    public void isAfterSiblingTest() {
        Path el = BasicPath.element.afterSibling(BasicPath.div.withClass("a"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("b"));
        assertThat(el.toString(), is(equalTo("any element, after the sibling (div, that has class a)")));
    }

    @Test
    public void isBeforeSiblingTest() {
        Path el = BasicPath.element.beforeSibling(BasicPath.span.withClass("abc"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(4));
        assertThat(el.toString(), is(equalTo("any element, before the sibling (span, that has class abc)")));
    }

    @Test
    public void isChildTest() {
        Path el = BasicPath.element.that(isChildOf(BasicPath.div));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(3));
        assertThat(getCssClass(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a.a"));
        assertThat(getCssClass(nodes.item(2)), equalTo("b"));
        assertThat(el.toString(), is(equalTo("any element, that is child of: div")));
    }

    @Test
    public void hasParentTest() {
        Path el = BasicPath.element.childOf(BasicPath.div);
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(3));
        assertThat(getCssClass(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a.a"));
        assertThat(getCssClass(nodes.item(2)), equalTo("b"));
        assertThat(el.toString(), is(equalTo("any element, child of div")));
    }

    @Test
    public void isParentTest() {
        Path el = BasicPath.element.that(hasClass("container").or(hasClass("a"))).inside(BasicPath.body.withClass("bar")).inside(BasicPath.html).parentOf(BasicPath.div);
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<body class=\"bar\"><div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span></body>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("any element, (has class container or has class a), inside (document body, that has class bar), inside document, parent of div")));
    }

    @Test
    public void hasChildTest() {
        Path el = BasicPath.element.inside(BasicPath.html).that(hasChild(BasicPath.div));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("any element, inside document, and is parent of: div")));
    }

    @Test
    public void isDescendantTest() {
        Path el = BasicPath.div.descendantOf(BasicPath.div.withClass("container"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a.a"));
        assertThat(el.toString(), is(equalTo("div, descendant of div, that has class container")));
    }

    @Test
    public void isInsideTest() {
        Path el = BasicPath.div.inside(BasicPath.div.withClass("container"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a.a"));
        assertThat(el.toString(), is(equalTo("div, inside (div, that has class container)")));
    }

    @Test
    public void isAncestorOfTest() {
        Path el = BasicPath.div.ancestorOf(BasicPath.div.withClass("a.a"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("div, ancestor of div, that has class a.a")));
    }

    @Test
    public void containingTest() {
        Path el = BasicPath.div.containing(BasicPath.div.withClass("a.a"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("div, ancestor of div, that has class a.a")));
    }

    @Test
    public void containsTest() {
        Path el = BasicPath.div.contains(BasicPath.div.withClass("a.a"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("div, ancestor of div, that has class a.a")));
    }

    @Test
    public void indexTest() {
        Path el = BasicPath.div.withIndex(1);
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(el.toString(), is(equalTo("div, with the index 1")));
    }

    @Test
    public void withTextTest() {
        Path el = BasicPath.div.withText("AB");
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>ab</div><div>abc</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("ab"));
        assertThat(el.toString(), is(equalTo("div, that has the text \"AB\"")));
    }

    @Test
    public void withClassesTest() {
        Path el = BasicPath.div.withClasses("a", "b");
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class=\"a\">a</div><div class=\"b\">b</div><div class=\"a b\">a b</div>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("a b"));
        assertThat(el.toString(), is(equalTo("div, that has classes [a, b]")));
    }

    @Test
    public void withTextContainingTest() {
        Path el = BasicPath.div.withTextContaining("AB");
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>ab</div><div>xabc</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getText(nodes.item(0)), equalTo("ab"));
        assertThat(getText(nodes.item(1)), equalTo("xabc"));
        assertThat(el.toString(), is(equalTo("div, that has text containing \"AB\"")));
    }

    @Test
    public void withDescriptionAndAdditionalProperty(){
        Path el = BasicPath.div.ancestorOf(BasicPath.div.withClass("foo").describedBy("abc").withClass("a.a"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='foo a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("div, ancestor of abc, that has class a.a")));
    }

    @Test
    public void withDescriptionAndAdditionalProperty2(){
        Path el = BasicPath.div.ancestorOf(BasicPath.div.that(hasClass("foo")).describedBy("abc").that(hasClass("a.a")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='foo a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("div, ancestor of abc, that has class a.a")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalOperationTest() {
        BasicPath.div.or(new BasicPath.PathBuilder().build());
    }

    @Test
    public void bareboneTostring() {
        BasicPath path = new BasicPath.PathBuilder().withXpath("Johhny").build();
        assertThat(path.toString(), is(equalTo("xpath: \"Johhny\"")));
    }

    @Test
    public void underlyingToString() {
        BasicPath path = new BasicPath.PathBuilder().withXpath("Johhny").withUnderlying(mock(WebElement.class)).build();
        assertThat(path.toString(), startsWith("under reference element Mock for WebElement"));
        assertThat(path.toString(), endsWith("xpath: \"Johhny\""));
    }

    @Test
    public void ToStringWith2ElementProperties() {
        Path path = BasicPath.anchor.that(hasClass("foo"), hasClass("bar"));
        assertThat(path.toString(), endsWith("anchor, that has class foo, and has class bar"));
    }

    @Test
    public void ToStringWithDescription() {
        Path path = BasicPath.anchor.withClass("x").describedBy("foo");
        assertThat(path.toString(), endsWith("foo"));
    }

    @Test(expected = UnsupportedOperationException.class)
      public void notDivBeforeSpanIsInvalid() {
        Path el = PathOperators.not(BasicPath.div.before(BasicPath.span));
    }

    @Test
    public void notDivWithClass() {
        Path el = PathOperators.not(BasicPath.div.withClass("foo"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class='foo'>foo</div><span></span>><div>boo</div>", xpath);
        assertThat(nodes.getLength(), is(3));  // this includes the artificial "HTML" that we add in findAllByXpath()
        assertThat(getText(nodes.item(2)), equalTo("boo"));
        assertThat(getElementName(nodes.item(1)), equalTo("span"));
        assertThat(getElementName(nodes.item(0)), equalTo("html"));
        assertThat(el.toString(), is(equalTo("anything except (div, that has class foo)")));

    }

    @Test
    public void notDivInsideSpan() {
        Path el = PathOperators.not(BasicPath.div).inside(BasicPath.span);
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<span><div class='foo' />foo<a></a></span><span></span>><div>boo</div>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getElementName(nodes.item(0)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("anything except (div), inside span")));

    }

    @Test(expected = UnsupportedOperationException.class)
    public void notDivInsideSpanRelationIsInvalid() {
        Path el = PathOperators.not(BasicPath.div.inside(BasicPath.span));
    }

}
