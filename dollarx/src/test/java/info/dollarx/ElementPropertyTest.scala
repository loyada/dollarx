package info.dollarx

import java.util.logging.Logger

import info.dollarx.ElementProperties.has.not.{text, textEqualTo, children, cssClass}
import org.junit.Test
import org.junit.Assert.assertThat
import org.mockito.Mockito.mock
import org.hamcrest.CoreMatchers.equalTo
import Path._
import ElementProperties._
import org.openqa.selenium.WebElement


class ElementPropertyTest extends XPathTester {
  val logger: Logger = Logger.getLogger(classOf[ElementPropertyTest].getName)

  def logit(p: Path) {
    logger.info(p.getXPath.get)
  }

  @Test def divThatBeforeSpan() {
    val el: Path = div that (is before span)
    logit(el)
    val nodes = findAllByXpath("<div>foo</div><div>boo</div><span></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("foo"))
    assertThat(getText(nodes.item(1)), equalTo("boo"))
    assertThat(el.toString, equalTo("div, that is before: span"))
  }

  @Test def spanThatAfterDiv() {
    val el: Path = span that (is after div)
   logit(el)
    val nodes = findAllByXpath("<div>foo</div><div>boo</div><span>xyz</span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("xyz"))
    assertThat(el.toString, equalTo("span, that is after: div"))
  }

  @Test def divThatBeforeSpanWithClass() {
    val el: Path = div that(is before span, has cssClass "d")
    logit(el)
    val nodes = findAllByXpath("<div class=\"a b\">foo</div><div  class=\"c d\">boo</div><span></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("boo"))
    assertThat(el.toString, equalTo( """div, that is before: span, and has class "d""""))
  }

  @Test def divThatBeforeSpanWithClasses() {
    val el: Path = div that(is before span, has classes("d", "f"))
   logit(el)
    val nodes = findAllByXpath("<div class=\"a b\">foo</div><div  class=\"c d\">boo</div><span></span>", el)
    assertThat(nodes.getLength, equalTo(0))
    assertThat(el.toString, equalTo("div, that is before: span, and has classes [d, f]"))
  }

  @Test def divWithOneOfClasses() {
    val el: Path = div.that(is before span, has oneOfClasses("d", "f"))
    logit(el)
    val nodes = findAllByXpath("<div class=\"a b f\">foo</div><div  class=\"c d\">boo</div><span></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("foo"))
    assertThat(getText(nodes.item(1)), equalTo("boo"))
    assertThat(el.toString, equalTo("div, that is before: span, and has at least one of the classes: [d, f]"))
  }

  @Test def isLastTest()() {
    val el: Path = (element inside html) that (is lastSibling)
    logit(el)
    val nodes = findAllByXpath("<div class=\"a b f\">foo</div><div  class=\"c d\">boo</div><span><a/><b/></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getElementName(nodes.item(0)), equalTo("span"))
    assertThat(getElementName(nodes.item(1)), equalTo("b"))
    assertThat(el.toString, equalTo("any element, inside document, and is last sibling"))
  }

  @Test def hasNoChildren() {
    val el: Path = element that (has no children)
    logit(el)
    val nodes = findAllByXpath("<div><x/>foo</div><div>foo</div><span><a/><b/></span>", el)
    assertThat(nodes.getLength, equalTo(4))
    assertThat(getElementName(nodes.item(0)), equalTo("x"))
    assertThat(getElementName(nodes.item(1)), equalTo("div"))
    assertThat(getElementName(nodes.item(2)), equalTo("a"))
    assertThat(getElementName(nodes.item(3)), equalTo("b"))
    assertThat(el.toString, equalTo("any element, that has no children"))
  }

  @Test def hasTwoChildren() {
    val el: Path = element.that(has(2) children)
    logit(el)
    val nodes = findAllByXpath("<div><x/>foo</div><div>foo</div><span><a/><b/></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getElementName(nodes.item(0)), equalTo("span"))
    assertThat(el.toString, equalTo("any element, that has 2 children"))
  }

  @Test def hasSomeChildren() {
    val el: Path = div.that(has children)
    logit(el)
    val nodes = findAllByXpath("<div class='container'><x/>foo</div><div>foo</div><span><a/><b/></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(el.toString, equalTo("div, that has some children"))
  }

  @Test def hasTwoOrMoreChildren() {
    val el: Path = div that (has(2 orMore) children)
    logit(el)
    val nodes = findAllByXpath("<div class='1-children' ><x/>foo</div> <div class='no-children'>foo</div> <div class='3-children'><a/><b/><a/></div> <div class='2-children'><a/><b/></div> <span><a/><b/></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("3-children"))
    assertThat(getCssClass(nodes.item(1)), equalTo("2-children"))
    assertThat(el.toString, equalTo("div, that has at least 2 children"))
  }

  @Test def hasTwoOrLessChildren() {
    val el: Path = div that (has(2 orLess) children)
    logit(el)
    val nodes = findAllByXpath("<div class='1-children' ><x/>foo</div> <div class='no-children'>foo</div> <div class='3-children'><a/><b/><a/></div> <div class='2-children'><a/><b/></div> <span><a/><b/></span>", el)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getCssClass(nodes.item(0)), equalTo("1-children"))
    assertThat(getCssClass(nodes.item(1)), equalTo("no-children"))
    assertThat(getCssClass(nodes.item(2)), equalTo("2-children"))
    assertThat(el.toString, equalTo("div, that has at most 2 children"))
  }

  @Test def hasTwoDescendants() {
    val el: Path = div that (has(2) descendants)
    logit(el)
    val nodes = findAllByXpath("<div class='1-children' ><x/>foo</div> <div class='no-children'>foo</div> <div class='3-children'><a/><b/><a/></div> <div class='2-descendants'><a><b/></a></div> <span><a/><b/></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("2-descendants"))
    assertThat(el.toString, equalTo("div, that has 2 descendants"))
  }

  @Test def hasTwoOrMoreDescendants() {
    val el: Path = div that (has(2 orMore) descendants)
    logit(el)
    val nodes = findAllByXpath("<div class='1-children' ><x/>foo</div> <div class='no-children'>foo</div> <div class='3-children'><a/><b/><a/></div> <div class='2-descendants'><a><b/></a></div> <span><a/><b/></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("3-children"))
    assertThat(getCssClass(nodes.item(1)), equalTo("2-descendants"))
    assertThat(el.toString, equalTo("div, that has at least 2 descendants"))
  }

  @Test def hasTwoOrLessDescendants() {
    val el: Path = div that (has(2 orLess) descendants)
    logit(el)
    val nodes = findAllByXpath("<div class='1-children' ><x/>foo</div> <div class='no-children'>foo</div> <div class='3-children'><a/><b/><a/></div> <div class='2-descendants'><a><b/></a></div> <span><a/><b/></span>", el)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getCssClass(nodes.item(0)), equalTo("1-children"))
    assertThat(getCssClass(nodes.item(1)), equalTo("no-children"))
    assertThat(getCssClass(nodes.item(2)), equalTo("2-descendants"))
    assertThat(el.toString, equalTo("div, that has at most 2 descendants"))
  }

  @Test def hasTwoSiblings() {
    val el: Path = div that (has(2) siblings)
    logit(el)
    val nodes = findAllByXpath("<a><div class='none'/></a> <a><div class='one'/><span/></a> <a><div class='two'/><div class='two'/><span/></a> <a><b/><b/><div class='three'/><span/></a>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("two"))
    assertThat(getCssClass(nodes.item(1)), equalTo("two"))
    assertThat(el.toString, equalTo("div, that has 2 siblings"))
  }

  @Test def hasTwoOrMoreSiblings() {
    val el: Path = div that (has(2 orMore) siblings)
    logit(el)
    val nodes = findAllByXpath("<a><div class='none'/></a> <a><div class='one'/><span/></a> <a><div class='two'/><div class='two'/><span/></a> <a><b/><b/><div class='three'/><span/></a>", el)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getCssClass(nodes.item(0)), equalTo("two"))
    assertThat(getCssClass(nodes.item(1)), equalTo("two"))
    assertThat(getCssClass(nodes.item(2)), equalTo("three"))
    assertThat(el.toString, equalTo("div, that has at least 2 siblings"))
  }

  @Test def hasTwoOrLessSiblings() {
    val el: Path = div that (has(2 orLess) siblings)
    logit(el)
    val nodes = findAllByXpath("<a><div class='none'/></a> <a><div class='one'/><span/></a> <a><div class='two'/><div class='two'/><span/></a> <a><b/><b/><div class='three'/><span/></a>", el)
    assertThat(nodes.getLength, equalTo(4))
    assertThat(getCssClass(nodes.item(0)), equalTo("none"))
    assertThat(getCssClass(nodes.item(1)), equalTo("one"))
    assertThat(getCssClass(nodes.item(2)), equalTo("two"))
    assertThat(getCssClass(nodes.item(3)), equalTo("two"))
    assertThat(el.toString, equalTo("div, that has at most 2 siblings"))
  }


  @Test def inRange() {
    val el: Path = listItem.that(is withIndexInRange(2, 4))
    logit(el)
    val nodes = findAllByXpath("<li>1</li><li>2</li><li>3</li><li>4</li><li>5</li>", el)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getText(nodes.item(0)), equalTo("3"))
    assertThat(getText(nodes.item(1)), equalTo("4"))
    assertThat(getText(nodes.item(2)), equalTo("5"))
    assertThat(el.toString, equalTo("list item, with index from 2 to 4"))
  }

  @Test def hasTextTest() {
    val el: Path = div.that(hasText("abc"))
    logit(el)
    val nodes = findAllByXpath("<div>abcd</div><div>ABC</div><span>abc</span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("ABC"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("div, that has the text \"abc\""))
  }

  @Test def hasAggrgatedTextTest() {
    val el: Path = div that (has aggregatedText "ABCD")
    logit(el)
    val nodes = findAllByXpath("<div>aBcd</div><div><span>ab</span><div>cd</div></div><span>abc</span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("aBcd"))
    assertThat(getText(nodes.item(1).getFirstChild), equalTo("ab"))
    assertThat(el.toString, equalTo("div, with aggregated text \"ABCD\""))
  }

  @Test def hasTextContainingTest() {
    val el: Path = div that (hasTextContaining("abC"))
    logit(el)
    val nodes = findAllByXpath("<div>abcd</div><div>ABdC</div><span>abc</span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("abcd"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("div, that has text containing \"abC\""))
  }

  @Test def hasAggrgatedTextContainsTest() {
    val el: Path = div that (has aggregatedTextContaining ("x y"))
    logit(el)
    val nodes = findAllByXpath("<div>x   yz</div><div><span>zx </span><div>Yy</div></div><span>abc</span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("x   yz"))
    assertThat(getText(nodes.item(1).getFirstChild), equalTo("zx "))
    assertThat(el.toString, equalTo("div, with aggregated text containing \"x y\""))
  }

  @Test def hasAttributeValueStringTest() {
    val el: Path = div that (has attributeWithValue("foo", "bar"))
    logit(el)
    val nodes = findAllByXpath("<div class=\"abc\" foo=\"bar\"></div><div class=\" foo \"></div><span class=\" foo \">abc</span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("abc"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("div, that has attribute foo: \"bar\""))
  }

  @Test def hasAttributeValueIntTest() {
    val el: Path = div that (has attributeWithValue("foo", 5))
    logit(el)
    val nodes = findAllByXpath("<div class=\"abc\" foo=\"bar\"></div><div class=\" foo \"></div><span class=\" foo \">abc</span><div class=\"xyz\" foo='5' />", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("xyz"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("div, that has attribute foo: \"5\""))
  }

  @Test def hasAttributeTest() {
    val el: Path = div that (has attribute "foo")
    logit(el)
    val nodes = findAllByXpath("<div class=\"abc\" foo=\"bar\"></div><div class=\" foo \"></div><span class=\" foo \">abc</span><script  class=\"xyz\" foo='5' />", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("abc"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("div, that has the attribute \"foo\""))
  }

  @Test def hasIdTest() {
    val el: Path = div that (has id "bar")
    logit(el)
    val nodes = findAllByXpath("<div class=\"abc\" id=\"bar\"></div><div class=\" foo \"></div><span class=\" foo \">abc</span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("abc"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("div, that has Id \"bar\""))
  }

  @Test def hasClassTest() {
    val el: Path = div that (has cssClass "foo")
    logit(el)
    val nodes = findAllByXpath("<div class=\"not-foo\"></div><div class=\" foo \"></div><span class=\" foo \">abc</span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo(" foo "))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo( """div, that has class "foo""""))
  }

  @Test def hasClassesTest() {
    val el: Path = div.that(hasClasses("not", "foo"))
    logit(el)
    val nodes = findAllByXpath("<div class=\"not-foo\"></div><div class=\" foo not\"></div><span class=\" foo \">abc</span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo(" foo not"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo( """div, that has classes [not, foo]"""))
  }

  @Test def hasAnyOfClassesTest() {
    val el: Path = element that (has oneOfClasses("bar", "foo"))
    logit(el)
    val nodes = findAllByXpath("<div class=\"not-foo\"></div><div class=\" foo not\"></div><span class=\" foo\">abc</span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo(" foo not"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(getCssClass(nodes.item(1)), equalTo(" foo"))
    assertThat(getElementName(nodes.item(1)), equalTo("span"))
    assertThat(el.toString, equalTo("any element, that has at least one of the classes: [bar, foo]"))
  }

  @Test def withoutClassesTest() {
    val el: Path = element.inside(html).that(has no cssClass("bar", "foo"))
    logit(el)
    val nodes = findAllByXpath("<div class=\"not-foo\"></div><div class=\" foo not\"></div><span class=\" foo\">abc</span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("not-foo"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("any element, inside document, and has non of the classes: [bar, foo]"))
  }

  @Test def withIndexTest() {
    val el: Path = div.that(is withIndex 1)
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div>b</div><div>c</div><div>d</div><span>abc</span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("b"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(el.toString, equalTo("div, with index 1"))
  }

  @Test def hasSomeTextTest() {
    val el: Path = div that (has someText)
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div></div><div>c</div><div></div><span>abc</span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(getText(nodes.item(1)), equalTo("c"))
    assertThat(el.toString, equalTo("div, that has some text"))
  }

  @Test def hasNoTextTest() {
    val el: Path = div that (has no text)
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div></div><div>c</div><div></div><span>abc</span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo(""))
    assertThat(getText(nodes.item(1)), equalTo(""))
    assertThat(el.toString, equalTo("div, that has no text"))
  }

  @Test def isHiddenTest() {
    val el: Path = div that (is hidden)
    logit(el)
    val nodes = findAllByXpath("<div style=\"foo:bar;display:none\">a</div><div></div><div>c</div><div></div><span>abc</span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(el.toString, equalTo("div, that is hidden"))
  }

  @Test def isChildTest() {
    val el: Path = element that (is childOf div)
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(getCssClass(nodes.item(2)), equalTo("b"))
    assertThat(el.toString, equalTo("any element, that is child of: div"))
  }

  @Test def hasParentTest() {
    val el: Path = element that (has parent div)
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(getCssClass(nodes.item(2)), equalTo("b"))
    assertThat(el.toString, equalTo("any element, that has parent: div"))
  }

  @Test def isParentTest() {
    val el: Path = (element inside html).that(is parentOf div)
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("any element, inside document, and has child: div"))
  }

  @Test def hasChildtTest() {
    val el: Path = (element inside html).that(has child div)
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("any element, inside document, and has child: div"))
  }

  @Test def hasSomeChildrenTest() {
    val el: Path = (element inside (html that (has children))) that (has child div)
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("any element, inside (document, that has some children), and has child: div"))
  }

  @Test def hasSomeChildrenNoMatchTest() {
    val el: Path = element that(has cssClass "a.a", has children)
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(0))
    assertThat(el.toString, equalTo( """any element, that [has class "a.a", has some children]"""))
  }

  @Test def isDescendantTest() {
    val el: Path = div.that(is descendantOf (div withClass "container"))
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(el.toString, equalTo( """div, that has ancestor: (div, that has class "container")"""))
  }

  @Test def hasAncestorTest() {
    val el: Path = div.that(hasAncesctor(div.withClass("container")))
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(el.toString, equalTo( """div, that has ancestor: (div, that has class "container")"""))
  }

  @Test def isInsideTest() {
    val el: Path = div that (is inside (div withClass "container"))
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(el.toString, equalTo( """div, that has ancestor: (div, that has class "container")"""))
  }

  @Test def isNotInsideTest() {
    import is._
    val el: Path = div that (is not inside(div withClass "container"))
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>x</div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(4))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("container"))
    assertThat(getText(nodes.item(2)), equalTo("c"))
    assertThat(getText(nodes.item(3)), equalTo("x"))
    assertThat(el.toString, equalTo( """div, that not (has ancestor: (div, that has class "container"))"""))
  }

  @Test def isContainedInTest() {
    val el: Path = div that (is containedIn (div withClass "container"))
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(el.toString, equalTo( """div, that has ancestor: (div, that has class "container")"""))
  }

  @Test def isAncestorOfTest() {
    val el: Path = div that (is ancestorOf (div withClass "a.a"))
    logit(el)
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo( """div, that has descendant: (div, that has class "a.a")"""))
  }

  @Test def isAncestorOfMultipleTest() {
    val el: Path = div that (is ancestorOf(div withClass "a.a", div withClass "a"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(el.toString, equalTo( """div, that has descendants: [(div, that has class "a.a"), (div, that has class "a")]"""))
  }

  @Test def hasDescendantsTest() {
    val el: Path = div.that(hasDescendant(div withClass "a.a"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo( """div, that has descendant: (div, that has class "a.a")"""))
  }

  @Test def hasDescendantVariationTest() {
    val el: Path = has descendant(div withClass "a.a", anchor) inside html
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><a></a><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(el.toString, equalTo( """any element, that has descendants: [(div, that has class "a.a"), anchor], inside document"""))
  }

  @Test def isBeforeTest() {
    val el: Path = is before (span withClass "abc")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(7))
    assertThat(el.toString, equalTo( """any element, that is before: (span, that has class "abc")"""))
  }

  @Test def isBeforeSiblingTest() {
    val el: Path = is beforeSibling (span withClass "abc")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(4))
    assertThat(el.toString, equalTo( """any element, that is before sibling: (span, that has class "abc")"""))
  }

  @Test def isBeforeSiblingMultipleTest() {
    val el: Path = element that (is beforeSibling(div withClass "a", span))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><bar/><div class='a'><div class='a.a'></div></div><span class='b'/><foo/></div><div>c</div><div>d</div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getElementName(nodes.item(0)), equalTo("bar"))
    assertThat(el.toString, equalTo( """any element, that is before siblings: [(div, that has class "a"), span]"""))
  }

  @Test def isBeforeSiblingWithCountTest() {
    val el: Path = is beforeSibling (2 occurrencesOf div)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><foo/><div class='a' /><div class='b'></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("container"))
    assertThat(getElementName(nodes.item(2)), equalTo("foo"))
    assertThat(el.toString, equalTo( """any element, that is before 2 occurrences of div siblings"""))
  }

  @Test def isAfterSingleTest() {
    val el: Path = element that (is after (div.withClass("container")))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getText(nodes.item(0)), equalTo("c"))
    assertThat(getText(nodes.item(1)), equalTo("d"))
    assertThat(getCssClass(nodes.item(2)), equalTo("abc"))
    assertThat(el.toString, equalTo( """any element, that is after: (div, that has class "container")"""))
  }

  @Test def isAfterMultiTest() {
    val el: Path = element that (is after(div withClass "container", div withClass "a.a", element withText "c"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("d"))
    assertThat(getCssClass(nodes.item(1)), equalTo("abc"))
    assertThat(el.toString, equalTo( """any element, that is after: [(div, that has class "container"), (div, that has class "a.a"), (any element, that has the text "c")]"""))
  }

  @Test def isAfterFlatTest() {
    val menuItem = div describedBy "menu item"
    val el: Path = is after (5 occurrencesOf menuItem)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div>b</div><div>c</div><div>d</div><div>e</div><a>f</a><span></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("f"))
    assertThat(getElementName(nodes.item(0)), equalTo("a"))
    assertThat(getElementName(nodes.item(1)), equalTo("span"))
    assertThat(el.toString, equalTo( """any element, that is after 5 occurrences of menu item"""))
  }

  @Test def isAfterWithHeirarchyTest() {
    val menuItem = div describedBy "menu item"
    val el: Path = is after (5 occurrencesOf menuItem)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a<div/></div><div>b<div/></div><div>c</div><div>d</div><div>e</div><a>f</a><span></span>", el)
    assertThat(nodes.getLength, equalTo(4))
    assertThat(getText(nodes.item(0)), equalTo("d"))
    assertThat(getText(nodes.item(1)), equalTo("e"))
    assertThat(getText(nodes.item(2)), equalTo("f"))
    assertThat(getElementName(nodes.item(3)), equalTo("span"))
    assertThat(el.toString, equalTo( """any element, that is after 5 occurrences of menu item"""))
  }

  @Test def isBeforeFlatTest() {
    val menuItem = div describedBy "menu item"
    val el: Path = is before (5 occurrencesOf menuItem)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<a></a><div>a</div><div>b</div><div>c</div><div>d</div><div>e</div><a>f</a><span></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getElementName(nodes.item(0)), equalTo("a"))
    assertThat(el.toString, equalTo( """any element, that is before 5 occurrences of menu item"""))
  }

  @Test def isBeforeWithHeirarchyTest() {
    val menuItem = div describedBy ("menu item")
    val el: Path = is before (5 occurrencesOf menuItem)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a<div/></div><div>b<div/></div><div>c</div><div>d</div><div>e</div><a>f</a><span></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(getText(nodes.item(1)), equalTo(""))
    assertThat(el.toString, equalTo( """any element, that is before 5 occurrences of menu item"""))
  }

  @Test def isAfterSiblingTest() {
    val el: Path = element that (is afterSibling (div withClass "a"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("b"))
    assertThat(el.toString, equalTo( """any element, that is after sibling: (div, that has class "a")"""))
  }

  @Test def isAfterSiblingMultipleTest() {
    val el: Path = element that (is afterSibling(div withClass "a", span))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/><foo/></div><div>c</div><div>d</div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getElementName(nodes.item(0)), equalTo("foo"))
    assertThat(el.toString, equalTo( """any element, that is after siblings: [(div, that has class "a"), span]"""))
  }

  @Test def isAfterSiblingWithCountTest() {
    val el: Path = is afterSibling (2 occurrencesOf div)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a' /><div class='b'></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(4))
    assertThat(getElementName(nodes.item(0)), equalTo("span"))
    assertThat(getCssClass(nodes.item(0)), equalTo("b"))
    assertThat(getText(nodes.item(1)), equalTo("c"))
    assertThat(getText(nodes.item(2)), equalTo("d"))
    assertThat(getCssClass(nodes.item(3)), equalTo("abc"))
    assertThat(el.toString, equalTo( """any element, that is after 2 occurrences of div siblings"""))
  }

  @Test def isFirstTest() {
    val el: Path = (div or span) that (is firstSibling)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"first\">a<span>a</span></div><div>b</div><div>c</div><div>d</div><div>e</div><a>f</a><span></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("first"))
    assertThat(getText(nodes.item(1)), equalTo("a"))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(getElementName(nodes.item(1)), equalTo("span"))
    assertThat(el.toString, equalTo( """div or span, with index 0"""))
  }

  @Test def notTest() {
    val el: Path = element that not(has text "a")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='abc'></div>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getElementName(nodes.item(0)), equalTo("html"))
    assertThat(getCssClass(nodes.item(1)), equalTo("abc"))
    assertThat(el.toString, equalTo( """any element, that not (has the text "a")"""))
  }

  @Test def hasNoTextEqualToTest {
    val el: Path = has no textEqualTo("a")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='abc'></div>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getElementName(nodes.item(0)), equalTo("html"))
    assertThat(getCssClass(nodes.item(1)), equalTo("abc"))
    assertThat(el.toString, equalTo( """any element, that has no text equal to "a""""))
  }

  @Test def andTest {
    val el: Path = element that ((has textContaining "x") and (has cssClass "abc"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class='abc'>a</div><div class='abc z'>xy</div>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("xy"))
    assertThat(getCssClass(nodes.item(0)), equalTo("abc z"))
    assertThat(el.toString, equalTo( """any element, (has text containing "x" and has class "abc")"""))
  }

  @Test def andNotTest {
    val el: Path = element.that(hasClass("abc").andNot(has textContaining "x"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class='abc'>a</div><div class='abc z'>xy</div>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(el.toString, equalTo( """any element, (has class "abc" and not (has text containing "x"))"""))
  }

  @Test def andAndNotTest {
    val el: Path = element.that(hasClass("abc").and(not(hasTextContaining("x"))))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class='abc'>a</div><div class='abc z'>xy</div>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(el.toString, equalTo( """any element, (has class "abc" and not (has text containing "x"))"""))
  }

  @Test def orTest {
    val el: Path = (has textContaining "x") or (has cssClass "abc")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class='abc'>a</div><div class='abc z'>xy</div>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(el.toString, equalTo( """any element, (has text containing "x" or has class "abc")"""))
  }

  @Test def isNthFromLastTest {
    val el: Path = div that ((has textContaining "x") and (is nthFromLastSibling 2))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"a\">w</div><div class=\"b\">x</div><div class=\"c\">y</div><div class=\"d\">z</div>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("b"))
    assertThat(el.toString, equalTo( """div, (has text containing "x" and is in place 2 from the last sibling)"""))
  }

  @Test def isNthSiblingTest {
    val el: Path = div that ((has textContaining "x") and (is nthSibling 2))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"a\">x</div><div class=\"b\">x</div><div class=\"c\">x</div><div class=\"d\">x</div>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("c"))
    assertThat(el.toString, equalTo( """div, (has text containing "x" and is in place 2 among its siblings)"""))
  }

  @Test def isOnlyChildTest {
    val el: Path = span that ((has textContaining "x") and (is onlyChild))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div><span> class=\"a\">x</span><span class=\"b\">x</span></div><div><span class=\"c\">x</span></div><div class=\"d\">x</div>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("c"))
    assertThat(el.toString, equalTo( """span, (has text containing "x" and is only child)"""))
  }

  @Test def hasSiblingTest {
    val el: Path = span that (has sibling (div withClass "a"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div><div class='b'></div><span class=\"a\"><span>a</span><div class=\"a\"></div><span>b</span></span></div>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("a"))
    assertThat(getText(nodes.item(1)), equalTo("b"))
    assertThat(el.toString, equalTo( """span, that has sibling: (div, that has class "a")"""))
  }

  @Test def isSiblingOfMultipleTest {
    val el: Path = element that (is siblingOf(div withClass "a", span withClass "a"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div><div class='b'></div><span class=\"a\"><span>a</span><div class=\"a\"></div><span>b</span></span></div><div><foo /><div class='a'/><span class='a' /><bar /></div>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getElementName(nodes.item(0)), equalTo("foo"))
    assertThat(getElementName(nodes.item(1)), equalTo("bar"))
    assertThat(el.toString, equalTo( """any element, that has siblings: [(div, that has class "a"), (span, that has class "a")]"""))
  }


  @Test def rawPropertyTest {
    val el: Path = span.that(raw("string(.)='x'", "is awesome"), is onlyChild)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div><span> class=\"a\">x</span><span class=\"b\">x</span></div><div><span class=\"c\">x</span></div><div class=\"d\">x</div>", el)
    assertThat(xpath, equalTo("span[string(.)='x'][count(preceding-sibling::*)=0 and count(following-sibling::*)=0]"))
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("c"))
    assertThat(el.toString, equalTo("span, that is awesome, and is only child"))
  }

  @Test(expected = classOf[IllegalArgumentException]) def invalidRelationTest {
    div that (has ancestor (new Path(underlyingSource = Some(mock(classOf[WebElement])))))
  }
}
