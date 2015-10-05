package info.testtools.jdollarx;

import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.w3c.dom.NodeList;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import static info.testtools.jdollarx.BasicPath.*;
import static info.testtools.jdollarx.ElementProperties.*;

public class ElementPropertiesTest extends XPathTester{

    @Test
    public void divBeforeSpan() {
        BasicPath el = div.before(span);
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>foo</div><div>boo</div><span></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getText(nodes.item(0)), equalTo("foo"));
        assertThat(getText(nodes.item(1)), equalTo("boo"));
        assertThat(xpath, is(equalTo("span/preceding::div")));
        assertThat(el.toString(), is(equalTo("div, before span")));
    }

    @Test
    public void divThatBeforeSpan() {
        Path el = div.that(isBefore(span));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>foo</div><div>boo</div><span></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getText(nodes.item(0)), equalTo("foo"));
        assertThat(getText(nodes.item(1)), equalTo("boo"));
        assertThat(el.toString(), is(equalTo("div, that is before: span")));
    }

    @Test
    public void spanThatAfterDiv() {
        Path el = span.that(isAfter(div));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>foo</div><div>boo</div><span>xyz</span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("xyz"));
        assertThat(el.toString(), is(equalTo("span, that is after: div")));
    }

    @Test
    public void divThatBeforeSpanWithClass() {
        Path el = div.that(isBefore(span), hasClass("d"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class=\"a b\">foo</div><div  class=\"c d\">boo</div><span></span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("boo"));

        assertThat(el.toString(), is(equalTo("div, that is before: span, and has class d")));
    }

    @Test
    public void divThatBeforeSpanWithClasses() {
        Path el = div.that(isBefore(span), hasClasses("d", "f"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class=\"a b\">foo</div><div  class=\"c d\">boo</div><span></span>", xpath);
        assertThat(nodes.getLength(), is(0));

        assertThat(el.toString(), is(equalTo("div, that is before: span, and has classes [d, f]")));
    }

    @Test
    public void divWithOneOfClasses() {
        Path el = div.that(isBefore(span), hasAnyOfClasses("d", "f"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class=\"a b f\">foo</div><div  class=\"c d\">boo</div><span></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getText(nodes.item(0)), equalTo("foo"));
        assertThat(getText(nodes.item(1)), equalTo("boo"));
        assertThat(el.toString(), is(equalTo("div, that is before: span, and has at least one of the classes [d, f]")));
    }

    @Test
    public void isLast() {
        Path el = element.inside(html).that(isLast);
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class=\"a b f\">foo</div><div  class=\"c d\">boo</div><span><a/><b/></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getElementName(nodes.item(0)), equalTo("span"));
        assertThat(getElementName(nodes.item(1)), equalTo("b"));
        assertThat(el.toString(), is(equalTo("any element, inside document, and is last sibling")));
    }

    @Test
    public void hasNoChildren() {
        Path el = element.that(hasNoChildren);
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div><x/>foo</div><div>foo</div><span><a/><b/></span>", xpath);
        assertThat(nodes.getLength(), is(4));
        assertThat(getElementName(nodes.item(0)), equalTo("x"));
        assertThat(getElementName(nodes.item(1)), equalTo("div"));
        assertThat(getElementName(nodes.item(2)), equalTo("a"));
        assertThat(getElementName(nodes.item(3)), equalTo("b"));

        assertThat(el.toString(), is(equalTo("any element, that has no children")));
    }

    @Test
    public void hasSomeChildren() {
        Path el = element.that(hasNChildren(2));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div><x/>foo</div><div>foo</div><span><a/><b/></span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getElementName(nodes.item(0)), equalTo("span"));
        assertThat(el.toString(), is(equalTo("any element, that has 2 children")));
    }

    @Test
    public void inRange() {
        Path el = listItem.that(withIndexInRange(2, 4));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<li>1</li><li>2</li><li>3</li><li>4</li><li>5</li>", xpath);
        assertThat(nodes.getLength(), is(3));
        assertThat(getText(nodes.item(0)), equalTo("3"));
        assertThat(getText(nodes.item(1)), equalTo("4"));
        assertThat(getText(nodes.item(2)), equalTo("5"));
        assertThat(el.toString(), is(equalTo("list item, with index from 2 to 4")));
    }

    @Test
    public void hasTextTest() {
        Path el = div.that(hasText("abc"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>abcd</div><div>ABC</div><span>abc</span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("ABC"));
        assertThat(getElementName(nodes.item(0)), equalTo("div"));
        assertThat(el.toString(), is(equalTo("div, that has the text \"abc\"")));
    }

    @Test
    public void hasAggrgatedTextTest() {
        Path el = div.that(hasAggregatedTextEqualTo("ABCD"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>aBcd</div><div><span>ab</span><div>cd</div></div><span>abc</span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getText(nodes.item(0)), equalTo("aBcd"));
        assertThat(getText(nodes.item(1).getFirstChild()), equalTo("ab"));
        assertThat(el.toString(), is(equalTo("div, with aggregated text \"ABCD\"")));
    }

    @Test
    public void hasTextContainingTest() {
        Path el = div.that(hasTextContaining("abC"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>abcd</div><div>ABdC</div><span>abc</span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("abcd"));
        assertThat(getElementName(nodes.item(0)), equalTo("div"));
        assertThat(el.toString(), is(equalTo("div, that has text containing \"abC\"")));
    }

    @Test
    public void hasAggrgatedTextContainsTest() {
        Path el = div.that(hasAggregatedTextContaining("x y"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>x   yz</div><div><span>zx </span><div>Yy</div></div><span>abc</span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getText(nodes.item(0)), equalTo("x   yz"));
        assertThat(getText(nodes.item(1).getFirstChild()), equalTo("zx "));
        assertThat(el.toString(), is(equalTo("div, with aggregated text containing \"x y\"")));
    }

    @Test
    public void hasAttributeTest() {
        Path el = div.that(hasAttribute("foo", "bar"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class=\"abc\" foo=\"bar\"></div><div class=\" foo \"></div><span class=\" foo \">abc</span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("abc"));
        assertThat(getElementName(nodes.item(0)), equalTo("div"));
        assertThat(el.toString(), is(equalTo("div, that has foo: \"bar\"")));
    }

    @Test
    public void hasIdTest() {
        Path el = div.that(hasId("bar"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class=\"abc\" id=\"bar\"></div><div class=\" foo \"></div><span class=\" foo \">abc</span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("abc"));
        assertThat(getElementName(nodes.item(0)), equalTo("div"));
        assertThat(el.toString(), is(equalTo("div, that has Id \"bar\"")));
    }

    @Test
    public void hasClassTest() {
        Path el = div.that(hasClass("foo"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class=\"not-foo\"></div><div class=\" foo \"></div><span class=\" foo \">abc</span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo(" foo "));
        assertThat(getElementName(nodes.item(0)), equalTo("div"));
        assertThat(el.toString(), is(equalTo("div, that has class foo")));
    }

    @Test
    public void hasClassesTest() {
        Path el = div.that(hasClasses("not", "foo"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class=\"not-foo\"></div><div class=\" foo not\"></div><span class=\" foo \">abc</span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo(" foo not"));
        assertThat(getElementName(nodes.item(0)), equalTo("div"));
        assertThat(el.toString(), is(equalTo("div, that has classes [not, foo]")));
    }

    @Test
    public void hasAnyOfClassesTest() {
        Path el = element.that(hasAnyOfClasses("bar", "foo"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class=\"not-foo\"></div><div class=\" foo not\"></div><span class=\" foo\">abc</span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo(" foo not"));
        assertThat(getElementName(nodes.item(0)), equalTo("div"));
        assertThat(getCssClass(nodes.item(1)), equalTo(" foo"));
        assertThat(getElementName(nodes.item(1)), equalTo("span"));
        assertThat(el.toString(), is(equalTo("any element, that has at least one of the classes [bar, foo]")));
    }

    @Test
    public void withoutClassesTest() {
        Path el = element.inside(html).that(hasNonOfTheClasses("bar", "foo"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class=\"not-foo\"></div><div class=\" foo not\"></div><span class=\" foo\">abc</span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("not-foo"));
        assertThat(getElementName(nodes.item(0)), equalTo("div"));
        assertThat(el.toString(), is(equalTo("any element, inside document, and has non of the classes [bar, foo]")));
    }

    @Test
    public void withIndexTest() {
        Path el = div.that(withIndex(1));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div>b</div><div>c</div><div>d</div><span>abc</span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("b"));
        assertThat(getElementName(nodes.item(0)), equalTo("div"));
        assertThat(el.toString(), is(equalTo("div, with index 1")));
    }

    @Test
    public void hasSomeTextTest() {
        Path el = div.that(hasSomeText);
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div></div><div>c</div><div></div><span>abc</span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getText(nodes.item(0)), equalTo("a"));
        assertThat(getText(nodes.item(1)), equalTo("c"));
        assertThat(el.toString(), is(equalTo("div, that has some text")));
    }

    @Test
    public void isHiddenTest() {
        Path el = div.that(isHidden);
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div style=\"foo:bar;display:none\">a</div><div></div><div>c</div><div></div><span>abc</span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("div, that is hidden")));
    }

    @Test
    public void isChildTest() {
        Path el = element.that(isChildOf(div));
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
        Path el = element.that(hasParent(div));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(3));
        assertThat(getCssClass(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a.a"));
        assertThat(getCssClass(nodes.item(2)), equalTo("b"));
        assertThat(el.toString(), is(equalTo("any element, that is child of: div")));
    }

    @Test
    public void isParentTest() {
        Path el = element.inside(html).that(isParentOf(div));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("any element, inside document, and has child: div")));
    }

    @Test
    public void hasChildtTest() {
        Path el = element.inside(html).that(hasChild(div));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("any element, inside document, and has child: div")));
    }
    @Test
    public void hasChildrentTest() {
        Path el = element.inside(html).that(hasChild(div, span));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(el.toString(), is(equalTo("any element, inside document, and has children: [div, span]")));
    }

    @Test
    public void hasSomeChildrenTest() {
        Path el = element.inside(html.that(hasChildren)).that(hasChild(div));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("any element, inside (document, that has some children), and has child: div")));
    }

    @Test
    public void hasSomeChildrenNoMatchTest() {
        Path el = element.that(hasClass("a.a"), hasChildren);
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(0));
        assertThat(el.toString(), is(equalTo("any element, that [has class a.a, has some children]")));
    }

    @Test
    public void isDescendantTest() {
        Path el = div.that(isDescendantOf(div.withClass("container")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a.a"));
        assertThat(el.toString(), is(equalTo("div, that has ancestor: (div, that has class container)")));
    }

    @Test
    public void hasAncestorTest() {
        Path el = div.that(hasAncesctor(div.withClass("container")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a.a"));
        assertThat(el.toString(), is(equalTo("div, that has ancestor: (div, that has class container)")));
    }

    @Test
    public void isInsideTest() {
        Path el = div.that(isInside(div.withClass("container")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a.a"));
        assertThat(el.toString(), is(equalTo("div, that has ancestor: (div, that has class container)")));
    }

    @Test
    public void isContainedInTest() {
        Path el = div.that(isContainedIn(div.withClass("container")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a.a"));
        assertThat(el.toString(), is(equalTo("div, that has ancestor: (div, that has class container)")));
    }

    @Test
    public void isAncestorOfTest() {
        Path el = div.that(isAncestorOf(div.withClass("a.a")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("div, that has descendant: (div, that has class a.a)")));
    }

    @Test
    public void isAncestorOfMultipleTest() {
        Path el = div.that(isAncestorOf(div.withClass("a.a"), div.withClass("a")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(el.toString(), is(equalTo("div, that has descendants: [(div, that has class a.a), (div, that has class a)]")));
    }

    @Test
    public void hasDescendantsTest() {
        Path el = div.that(hasDescendant(div.withClass("a.a")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), equalTo("container"));
        assertThat(getCssClass(nodes.item(1)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("div, that has descendant: (div, that has class a.a)")));
    }

    @Test
    public void isBeforeTest() {
        Path el = element.that(isBefore(span.withClass("abc")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(7));
        assertThat(el.toString(), is(equalTo("any element, that is before: (span, that has class abc)")));
    }

    @Test
    public void isBeforeSiblingTest() {
        Path el = element.that(isBeforeSibling(span.withClass("abc")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(4));
        assertThat(el.toString(), is(equalTo("any element, that is before sibling: (span, that has class abc)")));
    }

    @Test
    public void isAfterTest() {
        Path el = element.that(isAfter(div.withClass("container")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(3));
        assertThat(getText(nodes.item(0)), equalTo("c"));
        assertThat(getText(nodes.item(1)), equalTo("d"));
        assertThat(getCssClass(nodes.item(2)), equalTo("abc"));
        assertThat(el.toString(), is(equalTo("any element, that is after: (div, that has class container)")));
    }

    @Test
    public void isAfterSiblingTest() {
        Path el = element.that(isAfterSibling(div.withClass("a")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), equalTo("b"));
        assertThat(el.toString(), is(equalTo("any element, that is after sibling: (div, that has class a)")));
    }

    @Test
    public void isSiblingOfTest() {
        Path el = element.that(isSiblingOf(div.withClass("a")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class='container'><div>a</div><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getElementName(nodes.item(0)), equalTo("div"));
        assertThat(getText(nodes.item(0)), equalTo("a"));
        assertThat(getCssClass(nodes.item(1)), equalTo("b"));
        assertThat(el.toString(), is(equalTo("any element, that has sibling: (div, that has class a)")));
    }

    @Test
    public void isSiblingOfMultiTest() {
        Path el = element.that(isSiblingOf(div.withClass("a"), div.withText("a"), span));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class='container'><div>a</div><p>123</p><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getElementName(nodes.item(0)), equalTo("p"));
        assertThat(getText(nodes.item(0)), equalTo("123"));
        assertThat(el.toString(), is(equalTo("any element, that has siblings: [(div, that has class a), (div, that has the text \"a\"), span]")));
    }

    @Test
    public void notTest() {
        Path el = element.that(not(hasText("a")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div>a</div><div class='abc'></div>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getElementName(nodes.item(0)), equalTo("html"));
        assertThat(getCssClass(nodes.item(1)), equalTo("abc"));
        assertThat(el.toString(), is(equalTo("any element, not (has the text \"a\")")));
    }

    @Test
    public void andTest() {
        Path el = element.that(hasTextContaining("x").and(hasClass("abc")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class='abc'>a</div><div class='abc z'>xy</div>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("xy"));
        assertThat(getCssClass(nodes.item(0)), equalTo("abc z"));
        assertThat(el.toString(), is(equalTo("any element, (has text containing \"x\" and has class abc)")));
    }

    @Test
    public void andNotTest() {
        Path el = element.that(hasClass("abc").andNot(hasTextContaining("x")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class='abc'>a</div><div class='abc z'>xy</div>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("any element, (has class abc and not (has text containing \"x\"))")));
    }

    @Test
    public void andAndNotTest() {
        Path el = element.that(hasClass("abc").and(not(hasTextContaining("x"))));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class='abc'>a</div><div class='abc z'>xy</div>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getText(nodes.item(0)), equalTo("a"));
        assertThat(el.toString(), is(equalTo("any element, (has class abc and not (has text containing \"x\"))")));
    }


    @Test
    public void orTest() {
        Path el = element.that(hasTextContaining("x").or(hasClass("abc")));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class='abc'>a</div><div class='abc z'>xy</div>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(el.toString(), is(equalTo("any element, (has text containing \"x\" or has class abc)")));
    }

    @Test
    public void isNthFromLastTest() {
        Path el = div.that(hasTextContaining("x").and(isNthFromLastSibling(2)));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class=\"a\">w</div><div class=\"b\">x</div><div class=\"c\">y</div><div class=\"d\">z</div>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), is("b"));
        assertThat(el.toString(), is(equalTo("div, (has text containing \"x\" and is in place 2 from the last sibling)")));
    }

    @Test
    public void isNthSiblingTest() {
        Path el = div.that(hasTextContaining("x").and(isNthSibling(2)));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div class=\"a\">x</div><div class=\"b\">x</div><div class=\"c\">x</div><div class=\"d\">x</div>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), is("c"));
        assertThat(el.toString(), is(equalTo("div, (has text containing \"x\" and is in place 2 among its siblings)")));
    }

    @Test
    public void isOnlyChildTest() {
        Path el = span.that(hasTextContaining("x").and(isOnlyChild));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div><span> class=\"a\">x</span><span class=\"b\">x</span></div><div><span class=\"c\">x</span></div><div class=\"d\">x</div>", xpath);
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), is("c"));
        assertThat(el.toString(), is(equalTo("span, (has text containing \"x\" and is only child)")));
    }

    @Test
    public void rawPropertyTest() {
        Path el = span.that(rawXpathProperty("string(.)='x'", "is awesome"),isOnlyChild);
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div><span> class=\"a\">x</span><span class=\"b\">x</span></div><div><span class=\"c\">x</span></div><div class=\"d\">x</div>", xpath);
        assertThat(xpath, is(equalTo("span[string(.)='x'][count(preceding-sibling::*)=0 and count(following-sibling::*)=0]")));
        assertThat(nodes.getLength(), is(1));
        assertThat(getCssClass(nodes.item(0)), is("c"));
        assertThat(el.toString(), is(equalTo("span, that is awesome, and is only child")));
    }

    @Test(expected=IllegalArgumentException.class)
    public void invalidRelationTest() {
        Path path = div.that(hasAncesctor(new PathBuilder().withUnderlying(mock(WebElement.class)).build()));
        path.getXPath();
    }

    @Test
    public void hasNameTest() {
        Path el = div.that(hasName("abc"));
        String xpath = el.getXPath().get();
        NodeList nodes = findAllByXpath("<div name=\"abc\" class=\"a\">x</div><div name=\"abcd\" class=\"b\">x</div><div class=\"c\">x</div><div name=\"abc\" class=\"d\">x</div>", xpath);
        assertThat(nodes.getLength(), is(2));
        assertThat(getCssClass(nodes.item(0)), is("a"));
        assertThat(getCssClass(nodes.item(1)), is("d"));

        assertThat(el.toString(), is(equalTo("div, that has name: \"abc\"")));
    }
}
