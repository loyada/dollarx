package com.github.loyada.dollarx

import java.util.logging.Logger

import com.github.loyada.dollarx.ElementProperties._
import com.github.loyada.dollarx.ElementProperties.is
import com.github.loyada.dollarx.Path._
import org.hamcrest.CoreMatchers._
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.mock
import org.openqa.selenium.WebElement
import org.w3c.dom.NodeList

class PathTest extends XPathTester {
  val logger: Logger = Logger.getLogger(classOf[PathTest].getName)

  def logit(p: Path) {
    logger.info(p.getXPath.get)
  }

  @Test def divAfterSpan() {
    val el = div after span
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>foo</div><span></span>><div>bar</div>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("bar"))
    assertThat(el.toString, equalTo("div, after span"))
  }

  @Test def divBeforeSpan() {
    val el = div before span
    val xpath = el.getXPath.get
    val nodes = findAllByXpath("<div>foo</div><span></span>><div>boo</div>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("foo"))
    assertThat(el.toString, equalTo("div, before span"))
  }

  @Test def isAfterSiblingTest() {
    val el = element afterSibling (div withClass "a")
    val xpath = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div>d</div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("b"))
    assertThat(el.toString, equalTo("""any element, after the sibling (div, that has class "a")"""))
  }

  @Test def isBeforeSiblingTest() {
    val el: Path = element beforeSibling (span withClass "abc")
    val xpath = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(4))
    assertThat(el.toString, equalTo("""any element, before the sibling (span, that has class "abc")"""))
  }

  @Test def simpleNoWithRelationTest {
    val e: Path = div before span
    val el: Path = PathOperators.not(e)
    logit(el)
    val nodes: NodeList = findAllByXpath("<body><div>abc</div><span></span><div>boo</div></body>", el)
    assertThat(nodes.getLength, equalTo(4))
    assertThat(getElementName(nodes.item(0)), equalTo("html")) // this is the one we inject artificially in the tests
    assertThat(getElementName(nodes.item(1)), equalTo("body"))
    assertThat(getElementName(nodes.item(2)), equalTo("span"))
    assertThat(getElementName(nodes.item(3)), equalTo("div"))
    assertThat(getText(nodes.item(3)), equalTo("boo"))
    assertThat(el.toString, equalTo("anything except (div, before span)"))
  }

  @Test def complexNoWithOrTest {
    val e: Path = (div before span inside body) or html
    val el: Path = PathOperators.not(e)
    logit(el)
    assertThat(el.getXPath.get, equalTo("*[not(self::*[(self::div[following::span][ancestor::body]) | (self::html)])]"))
    val nodes: NodeList = findAllByXpath("<body><div>abc</div><span></span><div>boo</div></body>", el)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getElementName(nodes.item(0)), equalTo("body"))
    assertThat(getElementName(nodes.item(1)), equalTo("span"))
    assertThat(getElementName(nodes.item(2)), equalTo("div"))
    assertThat(getText(nodes.item(2)), equalTo("boo"))
    assertThat(el.toString, equalTo("anything except ((div, before span, inside (document body)) or document)"))
  }

  @Test def insideTopLevelTest {
    val el: Path = (div before span).insideTopLevel
    val nodes: NodeList = findAllByXpath("<div>foo</div><span></span>><div>boo</div>", el)
    assertThat(el.getXPath.get, equalTo("//span/preceding::div"))
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("foo"))
    assertThat(el.toString, equalTo("div, before span"))
  }

  @Test def insideTopLevelMultipleTest {
    val el: Path = (div before span).insideTopLevel.insideTopLevel
    val nodes: NodeList = findAllByXpath("<div>foo</div><span></span>><div>boo</div>", el)
    assertThat(el.getXPath.get, equalTo("//span/preceding::div"))
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("foo"))
    assertThat(el.toString, equalTo("div, before span"))
  }

  @Test def insideTopLevelVariationTest {
    val el: Path = (div before span) (2) insideTopLevel
    val nodes: NodeList = findAllByXpath("<span/><div>foo</div><span></span><div>boo</div><span/><div class='3'/><span/><div/>", el)
    assertThat(el.getXPath.get, equalTo("(//span/preceding::div)[3]"))
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getElementName(nodes.item(0)), equalTo("div"))
    assertThat(getCssClass(nodes.item(0)), equalTo("3"))
    assertThat(el.toString, equalTo("occurrence number 3 of (div, before span)"))
  }

  @Test def lastTest {
    val el: Path = last occurrenceOf div
    val nodes: NodeList = findAllByXpath("<body><div>foo</div><div>bar</div></body><div>boo</div>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("boo"))
    assertThat(el.toString, equalTo("the last occurence of div"))
  }

  @Test def lastTest2 {
    val el: Path = last occurrenceOf div
    val nodes: NodeList = findAllByXpath("<body><div>foo</div><div>bar<div>boo</div></div></body>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("boo"))
    assertThat(el.toString, equalTo("the last occurence of div"))
  }

  @Test def isChildTest() {
    val el: Path = element that (is childOf div)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(getCssClass(nodes.item(2)), equalTo("b"))
    assertThat(el.toString, equalTo("any element, that is child of: div"))
  }

  @Test def hasParentTest() {
    val el: Path = element childOf div
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(3))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(getCssClass(nodes.item(2)), equalTo("b"))
    assertThat(el.toString, equalTo("any element, child of div"))
  }

  @Test def isParentTest() {
    //   val el: Path = (element that ((has cssClass "container") or (has cssClass "a"))).inside(body withClass "bar") inside html parentOf div
    val e: Path = (element that ((has cssClass "container") or (has cssClass "a"))).inside(body withClass "bar") inside html
    // the step in the line below creates an invalid xpath (see : [contains[ancestor::html]  ... should be "div/parent::*[ancestor::html][ancestor::body[contains....]]")
    val el: Path = e  parentOf div
    logit(e)
    logit(el)
    val nodes = findAllByXpath("<body class=\"bar\"><div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span></body>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("""any element, (has class "container" or has class "a"), inside (document body, that has class "bar"), inside document, parent of div"""))
  }

  @Test def hasChildTest() {
    val el: Path = (element inside html).that(has child div)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("any element, inside document, and has child: div"))
  }

  @Test def isDescendantTest() {
    val el: Path = div descendantOf (div withClass "container")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(el.toString, equalTo("""div, descendant of div, that has class "container""""))
  }

  @Test def isInsideTest() {
    val el: Path = div inside (div withClass "container")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("a"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a.a"))
    assertThat(el.toString, equalTo("""div, inside (div, that has class "container")"""))
  }

  @Test def isAncestorOfTest {
    val el: Path = div ancestorOf (div withClass "a.a")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("""div, ancestor of div, that has class "a.a""""))
  }

  @Test def containingTest() {
    val el: Path = div containing (div withClass "a.a")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("""div, ancestor of div, that has class "a.a""""))
  }

  @Test def childNumberTest() {
    val el: Path = childNumber(1) ofType (span withClass "a")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<span></span><span class='a x'>a<span class='a y'>b</span><span class='a z'>c</span></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("a x"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a y"))
    assertThat(el.toString, equalTo("child number 1 of type(span, that has class \"a\")"))
  }


  @Test def indexTest() {
    val el: Path = div(1)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(el.toString, equalTo("occurrence number 2 of div"))
  }

  @Test def indexVariationTest() {
    val el: Path = div(1)
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a" +
                                  "<div class='container'>" +
      "                                   <div class='a'>" +
      "                                       <div class='a.a'></div>" +
      "                                   </div>" +
      "                                   <span class='b'/>" +
      "                           </div>" +
      "                           <div>c</div>" +
      "                           <div></div>" +
      "                           <span class='abc'></span>" +
      "                         </div>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(el.toString, equalTo("occurrence number 2 of div"))
  }

  @Test def indexAndInsideNegativeTest {
    val el: Path = div(0) inside span
    val xpath = el.getXPath.get
    val nodes = findAllByXpath("<div>foo</div><span><div>bar</div></span>", el)
    assertThat(nodes.getLength, equalTo(0))
    assertThat(el.toString, equalTo("the first occurrence of div, inside span"))
  }

  @Test def nestedIndexTest {
    val aDiv = (div withClass "a" )(1)
    val el: Path = (span inside aDiv)(1)
    val xpath = el.getXPath.get
    val nodes = findAllByXpath("<div class='a'>foo" +
      "                            <span>1</span>" +
      "                            <span>2</span>" +
      "                         </div>" +
      "                         <div class='a'>" +
      "                            <span>5" +
      "                                <span>3</span>" +
      "                                <span>4</span>" +
      "                            </span>" +
      "                            <span>6</span>" +
      "                         </div>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("3"))
    assertThat(el.toString, equalTo("occurrence number 2 of (span, inside (occurrence number 2 of (div, that has class \"a\")))"))
  }

  @Test def withTextTest() {
    val el: Path = div withText "AB"
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>ab</div><div>abc</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("ab"))
    assertThat(el.toString, equalTo("div, that has the text \"AB\""))
  }

  @Test def hasTextTest() {
    val el: Path = div that (has text "AB")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>ab</div><div>abc</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("ab"))
    assertThat(el.toString, equalTo("""div, that has the text "AB""""))
  }

  @Test def firstTest() {
    val el: Path = first occurrenceOf div
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>ab</div><div>bc</div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("ab"))
    assertThat(el.toString, equalTo("""the first occurrence of div"""))
  }

  @Test def withClassesTest() {
    val el: Path = div.withClasses("a", "b")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"a\">a</div><div class=\"b\">b</div><div class=\"a b\">a b</div>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("a b"))
    assertThat(el.toString, equalTo("div, that has classes [a, b]"))
  }

  @Test def hasClassesTest() {
    val el: Path = div that (has classes("a", "b"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div class=\"a\">a</div><div class=\"b\">b</div><div class=\"a b\">a b</div>", el)
    assertThat(nodes.getLength, equalTo(1))
    assertThat(getText(nodes.item(0)), equalTo("a b"))
    assertThat(el.toString, equalTo("div, that has classes [a, b]"))
  }

  @Test def withTextContainingTest() {
    val el: Path = div.withTextContaining("AB")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>ab</div><div>xabc</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("ab"))
    assertThat(getText(nodes.item(1)), equalTo("xabc"))
    assertThat(el.toString, equalTo("div, that has text containing \"AB\""))
  }

  @Test def hasTextContainingTest() {
    val el: Path = div.that(has textContaining "AB")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>ab</div><div>xabc</div><div class='container'><div class='a'><div class='a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getText(nodes.item(0)), equalTo("ab"))
    assertThat(getText(nodes.item(1)), equalTo("xabc"))
    assertThat(el.toString, equalTo("div, that has text containing \"AB\""))
  }

  @Test def withDescriptionAndAdditionalProperty {
    val el: Path = div.ancestorOf(div.withClass("foo").describedBy("abc").withClass("a.a"))
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='foo a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("""div, ancestor of abc, that has class "a.a""""))
  }

  @Test def withDescriptionAndAdditionalProperty2 {
    val el: Path = div ancestorOf (div that (has cssClass "foo") describedBy "abc").that(has cssClass "a.a")
    val xpath: String = el.getXPath.get
    val nodes = findAllByXpath("<div>a</div><div class='container'><div class='a'><div class='foo a.a'></div></div><span class='b'/></div><div>c</div><div></div><span class='abc'></span>", el)
    assertThat(nodes.getLength, equalTo(2))
    assertThat(getCssClass(nodes.item(0)), equalTo("container"))
    assertThat(getCssClass(nodes.item(1)), equalTo("a"))
    assertThat(el.toString, equalTo("""div, ancestor of abc, that has class "a.a""""))
  }

  @Test(expected = classOf[IllegalArgumentException]) def illegalOperationTest() {
    div.or(new Path())
  }

  @Test def bareboneTostring() {
    val path = new Path(xpath = Some("Johhny"))
    assertThat(path.toString, equalTo("xpath: \"Johhny\""))
  }

  @Test def underlyingToString() {
    val path = new Path(xpath = Some("Johhny"), underlyingSource = (Some(mock(classOf[WebElement]))))
    assertThat(path.toString, startsWith("under reference element Mock for WebElement"))
    assertThat(path.toString, endsWith("xpath: \"Johhny\""))
  }

  @Test def ToStringWith2ElementProperties() {
    val path: Path = anchor that(has cssClass "foo", has cssClass "bar")
    assertThat(path.toString, endsWith("""anchor, that has class "foo", and has class "bar""""))
  }

  @Test def ToStringWithDescription() {
    val path: Path = (anchor withClass "x") describedBy "foo"
    assertThat(path.toString, endsWith("foo"))
  }

}
