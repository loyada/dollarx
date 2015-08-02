package info.dollarx

import info.dollarx.ElementProperties.has.not.{text, textEqualTo, children, cssClass}
import org.junit.Test
import org.junit.Assert.assertThat
import org.mockito.Mockito.mock
import org.hamcrest.CoreMatchers.equalTo
import Path._
import ElementProperties._
import org.openqa.selenium.WebElement


class ElementPropertyTest extends XPathTester{

  @Test def divThatBeforeSpan() {
    val el: Path = div that(is before span)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>foo</div><div>boo</div><span></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("foo"))
    assertThat(getText(nodes.item(1)), equalTo("boo"))
    assertThat(el.toString, equalTo("div, that is before: span"))
  }

  @Test def spanThatAfterDiv() {
    val el: Path = span that(is after div)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>foo</div><div>boo</div><span>xyz</span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("xyz"))
    assertThat(el.toString, equalTo("span, that is after: div"))
  }

  @Test def divThatBeforeSpanWithClass() {
    val el: Path = div.that(isBefore(span), hasClass("d"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"a b\">foo</div><div  class=\"c d\">boo</div><span></span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("boo"))
    assertThat(el.toString, equalTo("""div, that is before: span, and has class "d""""))
  }

  @Test def divThatBeforeSpanWithClasses() {
    val el: Path = div.that(isBefore(span), hasClasses("d", "f"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"a b\">foo</div><div  class=\"c d\">boo</div><span></span>", xpath)
    assertThat(nodes.getLength, equalTo(0))
    assertThat(el.toString, equalTo("div, that is before: span, and has classes [d, f]"))
  }

  @Test def divWithOneOfClasses() {
    val el: Path = div.that(is before(span), has oneOfClasses ("d", "f"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"a b f\">foo</div><div  class=\"c d\">boo</div><span></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("foo"))
    assertThat(getText(nodes.item(1)), equalTo("boo"))
    assertThat(el.toString, equalTo("div, that is before: span, and has at least one of the classes: [d, f]"))
  }

  @Test def isLastTest()() {
    val el: Path = (element inside html) that(is lastSibling)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"a b f\">foo</div><div  class=\"c d\">boo</div><span><a/><b/></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getElementName(nodes.item(0)), equalTo("span"))
    assertThat(getElementName(nodes.item(1)), equalTo("b"))
    assertThat(el.toString, equalTo("any element, inside document, and is last sibling"))
  }

  @Test def hasNoChildren() {
    val el: Path = element that(has no children)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div><x/>foo</div><div>foo</div><span><a/><b/></span>", xpath)
    assertThat(nodes.getLength, equalTo(4))
    assertThat(getElementName(nodes.item(0)), equalTo("x"))
    assertThat(getElementName(nodes.item(1)), equalTo("div"))
    assertThat(getElementName(nodes.item(2)), equalTo("a"))
    assertThat(getElementName(nodes.item(3)), equalTo("b"))
    assertThat(el.toString, equalTo("any element, that has no children"))
  }

  @Test def hasSomeChildren() {
    val el: Path = element.that(has(2) children)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div><x/>foo</div><div>foo</div><span><a/><b/></span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getElementName(nodes.item(0)), equalTo("span"))
    assertThat(el.toString, equalTo("any element, that has 2 children"))
  }

  @Test def inRange() {
    val el: Path = listItem.that(is withIndexInRange (2, 4))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<li>1</li><li>2</li><li>3</li><li>4</li><li>5</li>", xpath)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getText(nodes.item(0)), equalTo("3"))
    assertThat(getText(nodes.item(1)), equalTo("4"))
    assertThat(getText(nodes.item(2)), equalTo("5"))
    assertThat(el.toString, equalTo("list item, with index from 2 to 4"))
  }

  @Test def hasTextTest() {
    val el: Path = div.that(hasText("abc"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>abcd</div><div>ABC</div><span>abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("ABC"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("div, that has the text \"abc\""))
  }

  @Test def hasAggrgatedTextTest() {
    val el: Path = div that(has aggregatedText ("ABCD"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>aBcd</div><div><span>ab</span><div>cd</div></div><span>abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("aBcd"))
    assertThat(getText(nodes.item(1).getFirstChild), equalTo("ab"))
    assertThat(el.toString, equalTo("div, with aggregated text \"ABCD\""))
  }

  @Test def hasTextContainingTest() {
    val el: Path = div that(hasTextContaining("abC"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>abcd</div><div>ABdC</div><span>abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("abcd"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("div, that has text containing \"abC\""))
  }

  @Test def hasAggrgatedTextContainsTest() {
    val el: Path = div that(has aggregatedTextContaining ("x y"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>x   yz</div><div><span>zx </span><div>Yy</div></div><span>abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("x   yz"))
    assertThat(getText(nodes.item(1).getFirstChild), equalTo("zx "))
    assertThat(el.toString, equalTo("div, with aggregated text containing \"x y\""))
  }

  @Test def hasAttributeTest() {
    val el: Path = div.that(has attribute("foo", "bar"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"abc\" foo=\"bar\"></div><div class=\" foo \"></div><span class=\" foo \">abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("abc"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("div, that has foo: \"bar\""))
  }

  @Test def hasIdTest() {
    val el: Path = div that(has id "bar")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"abc\" id=\"bar\"></div><div class=\" foo \"></div><span class=\" foo \">abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("abc"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("div, that has Id \"bar\""))
  }

  @Test def hasClassTest() {
    val el: Path = div that(has cssClass "foo")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"not-foo\"></div><div class=\" foo \"></div><span class=\" foo \">abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo(" foo "))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("""div, that has class "foo""""))
  }

  @Test def hasClassesTest() {
    val el: Path = div.that(hasClasses("not", "foo"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"not-foo\"></div><div class=\" foo not\"></div><span class=\" foo \">abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo(" foo not"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("""div, that has classes [not, foo]"""))
  }

  @Test def hasAnyOfClassesTest() {
    val el: Path = element that(has oneOfClasses ("bar", "foo"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"not-foo\"></div><div class=\" foo not\"></div><span class=\" foo\">abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo(" foo not"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(getCssClass(nodes.item(1)), equalTo(" foo"))
    assertThat(getElementName(nodes.item(1)), equalTo("span"))
    assertThat(el.toString, equalTo("any element, that has at least one of the classes: [bar, foo]"))
  }

  @Test def withoutClassesTest() {
    val el: Path = element.inside(html).that(has no cssClass("bar", "foo"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"not-foo\"></div><div class=\" foo not\"></div><span class=\" foo\">abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("not-foo"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("any element, inside document, and has non of the classes: [bar, foo]"))
  }

  @Test def withIndexTest() {
    val el: Path = div.that(is withIndex 1)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div>b</div><div>c</div><div>d</div><span>abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("b"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("div, with index 1"))
  }

  @Test def hasSomeTextTest() {
    val el: Path = div that(has someText)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div></div><div>c</div><div></div><span>abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(getText(nodes.item(1)), equalTo("c"))
    assertThat(el.toString, equalTo("div, that has some text"))
  }

  @Test def hasNoTextTest() {
    val el: Path = div that(has no text )
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div></div><div>c</div><div></div><span>abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo(""))
    assertThat(getText(nodes.item(1)), equalTo(""))
    assertThat(el.toString, equalTo("div, that has no text"))
  }

  @Test def isHiddenTest() {
    val el: Path = div that(is hidden)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div style=\"foo:bar;display:none\">a</div><div></div><div>c</div><div></div><span>abc</span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(el.toString, equalTo("div, that is hidden"))
  }

  @Test def isChildTest() {
    val el: Path = element that(is childOf div)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(getCssClass(nodes.item(2)), equalTo("b"))
    assertThat(el.toString, equalTo("any element, that is child of: div"))
  }

  @Test def hasParentTest() {
    val el: Path = element that(has parent div)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(getCssClass(nodes.item(2)), equalTo("b"))
    assertThat(el.toString, equalTo("any element, that has parent: div"))
  }

  @Test def isParentTest() {
    val el: Path = (element inside html).that(is parentOf div)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("any element, inside document, and has child: div"))
  }

  @Test def hasChildtTest() {
    val el: Path = (element inside html).that(has child div)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("any element, inside document, and has child: div"))
  }

  @Test def hasSomeChildrenTest() {
    val el: Path = (element inside (html that(has children))) that(has child div)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("any element, inside (document, that has some children), and has child: div"))
  }

  @Test def hasSomeChildrenNoMatchTest() {
    val el: Path = element that(has cssClass "a.a", has children)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(0))
    assertThat(el.toString, equalTo("""any element, that [has class "a.a", has some children]"""))
  }

  @Test def isDescendantTest() {
    val el: Path = div.that(is descendantOf(div withClass "container"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(el.toString, equalTo("""div, that has ancestor: (div, that has class "container")"""))
  }

  @Test def hasAncestorTest() {
    val el: Path = div.that(hasAncesctor(div.withClass("container")))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(el.toString, equalTo("""div, that has ancestor: (div, that has class "container")"""))
  }

  @Test def isInsideTest() {
    val el: Path = div that(is inside(div withClass "container"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(el.toString, equalTo("""div, that has ancestor: (div, that has class "container")"""))
  }

  @Test def isNotInsideTest() {
    import is._
    val el: Path = div that(is not inside(div withClass "container"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>x</div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(4))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("container"))
    assertThat(getText(nodes.item(2)), equalTo("c"))
    assertThat(getText(nodes.item(3)), equalTo("x"))
    assertThat(el.toString, equalTo("""div, that not (has ancestor: (div, that has class "container"))"""))
  }

  @Test def isContainedInTest() {
    val el: Path = div that(is containedIn (div withClass "container"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(el.toString, equalTo("""div, that has ancestor: (div, that has class "container")"""))
  }

  @Test def isAncestorOfTest() {
    val el: Path = div that(is ancestorOf  (div withClass "a.a"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("""div, that has descendant: (div, that has class "a.a")"""))
  }

  @Test def isAncestorOfMultipleTest() {
    val el: Path = div that(is ancestorOf (div withClass "a.a", div withClass "a"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(el.toString, equalTo("""div, that has descendants: [(div, that has class "a.a"), (div, that has class "a")]"""))
  }

  @Test def hasDescendantsTest() {
    val el: Path = div.that(hasDescendant(div.withClass("a.a")))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("""div, that has descendant: (div, that has class "a.a")"""))
  }

  @Test def isBeforeTest() {
    val el: Path = is before(span withClass "abc")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(7))
    assertThat(el.toString, equalTo("""any element, that is before: (span, that has class "abc")"""))
  }

  @Test def isBeforeSiblingTest() {
    val el: Path = is beforeSibling(span withClass "abc")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(4))
    assertThat(el.toString, equalTo("""any element, that is before sibling: (span, that has class "abc")"""))
  }
  @Test def isBeforeSiblingMultipleTest() {
    val el: Path = element that(is beforeSibling (div withClass "a", span))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><bar/><div class='a'><div class='a.a'></div></div><span class='b'/><foo/></div><div>c</div><div>d</div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getElementName(nodes.item(0)), equalTo("bar"))
    assertThat(el.toString, equalTo("""any element, that is before siblings: [(div, that has class "a"), span]"""))
  }

  @Test def isBeforeSiblingWithCountTest() {
    val el: Path = is beforeSibling (2 occurancesOf div)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><foo/><div class='a' /><div class='b'></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("container"))
    assertThat(getElementName(nodes.item(2)), equalTo("foo"))
    assertThat(el.toString, equalTo("""any element, that is before 2 occurances of div siblings"""))
  }

  @Test def isAfterSingleTest() {
    val el: Path = element that (is after(div.withClass("container")))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getText(nodes.item(0)), equalTo("c"))
    assertThat(getText(nodes.item(1)), equalTo("d"))
    assertThat(getCssClass(nodes.item(2)), equalTo("abc"))
    assertThat(el.toString, equalTo("""any element, that is after: (div, that has class "container")"""))
  }

  @Test def isAfterMultiTest() {
    val el: Path = element that (is after(div withClass "container", div withClass "a.a", element withText "c"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("d"))
    assertThat(getCssClass(nodes.item(1)), equalTo("abc"))
    assertThat(el.toString, equalTo("""any element, that is after: [(div, that has class "container"), (div, that has class "a.a"), (any element, that has the text "c")]"""))
  }

  @Test def isAfterFlatTest() {
    val menuItem = div describedBy("menu item")
    val el: Path = is after(5 occurancesOf menuItem)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div>b</div><div>c</div><div>d</div><div>e</div><a>f</a><span></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("f"))
    assertThat(getElementName(nodes.item(0)), equalTo("a"))
    assertThat(getElementName(nodes.item(1)), equalTo("span"))
    assertThat(el.toString, equalTo("""any element, that is after 5 occurances of menu item"""))
  }

  @Test def isAfterWithHeirarchyTest() {
    val menuItem = div describedBy("menu item")
    val el: Path = is after(5 occurancesOf menuItem)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a<div/></div><div>b<div/></div><div>c</div><div>d</div><div>e</div><a>f</a><span></span>", xpath)
    assertThat(nodes.getLength, equalTo(4))
    assertThat(getText(nodes.item(0)), equalTo("d"))
    assertThat(getText(nodes.item(1)), equalTo("e"))
    assertThat(getText(nodes.item(2)), equalTo("f"))
    assertThat(getElementName(nodes.item(3)), equalTo("span"))
    assertThat(el.toString, equalTo("""any element, that is after 5 occurances of menu item"""))
  }

  @Test def isBeforeFlatTest() {
    val menuItem = div describedBy("menu item")
    val el: Path = is before(5 occurancesOf menuItem)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<a></a><div>a</div><div>b</div><div>c</div><div>d</div><div>e</div><a>f</a><span></span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getElementName(nodes.item(0)), equalTo("a"))
    assertThat(el.toString, equalTo("""any element, that is before 5 occurances of menu item"""))
  }

  @Test def isBeforeWithHeirarchyTest() {
    val menuItem = div describedBy("menu item")
    val el: Path = is before(5 occurancesOf menuItem)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a<div/></div><div>b<div/></div><div>c</div><div>d</div><div>e</div><a>f</a><span></span>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(getText(nodes.item(1)), equalTo(""))
    assertThat(el.toString, equalTo("""any element, that is before 5 occurances of menu item"""))
  }

  @Test def isAfterSiblingTest() {
    val el: Path = element that(is afterSibling (div withClass "a"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("b"))
    assertThat(el.toString, equalTo("""any element, that is after sibling: (div, that has class "a")"""))
  }

  @Test def isAfterSiblingMultipleTest() {
    val el: Path = element that(is afterSibling (div withClass "a", span))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/><foo/></div><div>c</div><div>d</div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getElementName(nodes.item(0)), equalTo("foo"))
    assertThat(el.toString, equalTo("""any element, that is after siblings: [(div, that has class "a"), span]"""))
  }

  @Test def isAfterSiblingWithCountTest() {
    val el: Path = is afterSibling (2 occurancesOf div)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a' /><div class='b'></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", xpath)
    assertThat(nodes.getLength, equalTo(4))
    assertThat(getElementName(nodes.item(0)), equalTo("span"))
    assertThat(getCssClass(nodes.item(0)), equalTo("b"))
    assertThat(getText(nodes.item(1)), equalTo("c"))
    assertThat(getText(nodes.item(2)), equalTo("d"))
    assertThat(getCssClass(nodes.item(3)), equalTo("abc"))
    assertThat(el.toString, equalTo("""any element, that is after 2 occurances of div siblings"""))
  }

  @Test def notTest() {
    val el: Path = element.that(not(has text "a"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='abc'></div>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getElementName(nodes.item(0)), equalTo("html"))
    assertThat(getCssClass(nodes.item(1)), equalTo("abc"))
    assertThat(el.toString, equalTo("""any element, not (has the text "a")"""))
  }

  @Test def hasNoTextEqualToTest {
    val el: Path = has no textEqualTo("a")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='abc'></div>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getElementName(nodes.item(0)), equalTo("html"))
    assertThat(getCssClass(nodes.item(1)), equalTo("abc"))
    assertThat(el.toString, equalTo("""any element, that has no text equal to "a""""))
  }

  @Test def andTest {
    val el: Path = element.that((has textContaining "x") and( has cssClass "abc"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class='abc'>a</div><div class='abc z'>xy</div>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("xy"))
    assertThat(getCssClass(nodes.item(0)), equalTo("abc z"))
    assertThat(el.toString, equalTo("""any element, (has text containing "x" and has class "abc")"""))
  }

  @Test def andNotTest {
    val el: Path = element.that(hasClass("abc").andNot(has textContaining "x"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class='abc'>a</div><div class='abc z'>xy</div>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(el.toString, equalTo("""any element, (has class "abc" and not (has text containing "x"))"""))
  }

  @Test def andAndNotTest {
    val el: Path = element.that(hasClass("abc").and(not(hasTextContaining("x"))))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class='abc'>a</div><div class='abc z'>xy</div>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(el.toString, equalTo("""any element, (has class "abc" and not (has text containing "x"))"""))
  }

  @Test def orTest {
    val el: Path = (has textContaining "x") or (has cssClass "abc")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class='abc'>a</div><div class='abc z'>xy</div>", xpath)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(el.toString, equalTo("""any element, (has text containing "x" or has class "abc")"""))
  }

  @Test def isNthFromLastTest {
    val el: Path = div that((has textContaining "x") and (is nthFromLastSibling 2))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"a\">w</div><div class=\"b\">x</div><div class=\"c\">y</div><div class=\"d\">z</div>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("b"))
    assertThat(el.toString, equalTo("""div, (has text containing "x" and is in place 2 from the last sibling)"""))
  }

  @Test def isNthSiblingTest {
    val el: Path = div that((has textContaining "x") and (is nthSibling 2))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"a\">x</div><div class=\"b\">x</div><div class=\"c\">x</div><div class=\"d\">x</div>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("c"))
    assertThat(el.toString, equalTo("""div, (has text containing "x" and is in place 2 among its siblings)"""))
  }

  @Test def isOnlyChildTest {
    val el: Path = span that((has textContaining "x") and(is onlyChild))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div><span> class=\"a\">x</span><span class=\"b\">x</span></div><div><span class=\"c\">x</span></div><div class=\"d\">x</div>", xpath)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("c"))
    assertThat(el.toString, equalTo("""span, (has text containing "x" and is only child)"""))
  }

  @Test def rawPropertyTest {
    val el: Path = span.that(raw("string(.)='x'", "is awesome"), is onlyChild)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div><span> class=\"a\">x</span><span class=\"b\">x</span></div><div><span class=\"c\">x</span></div><div class=\"d\">x</div>", xpath)
    assertThat(xpath, equalTo("span[string(.)='x'][count(preceding-sibling::*)=0 and count(following-sibling::*)=0]"))
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("c"))
    assertThat(el.toString, equalTo("span, that is awesome, and is only child"))
  }

  @Test(expected = classOf[IllegalArgumentException]) def invalidRelationTest {
    div that(has ancestor(new Path(underlyingSource = Some(mock(classOf[WebElement])))))
  }
}
