package dollarx

import org.openqa.selenium.Keys

object AppTest extends App {
  import ElementProperties._
  import WebEl._
  import InBrowser._
   println(unorderedList withProperties (has cssClass("foo") or  (has.no cssClass("bar"))  and hasText("xxx"), hasChild(listItem)))
  val p2 = has classes("foo", "bar") and hasDescendant(div) and (has(5) children)
  println(p2)
  val el: WebEl = p2
  println(el)
  val menuItem = listItem withClass ("setting-value")
  println(span withProperties(withAggregatedTextContaining("danny")) descendantOf menuItem(3) )


  val p3 = (hasClass("foo") or  hasClass("bar")) andNot hasText("xxx")
  val p31 = has oneOfClasses ("foo", "bar") and  (has.no text("xxx"))
  println(p3)
  println(p31)
  println(p31.toXpath())


  val p4 = hasClass("foo") and childOf(WebEl("//div"))
  println(p4)

  val p5 = raw("blah and moo xxx") and hasClass("foo") withIndex (4)
  println(p5)
  println(p5.getXPath())


  val dialog = div withClass "ui-dialog"
  println(dialog)
  val myButton = button withClass("foo") withProperties(hasTextContaining("submit") or Not(isHidden)) inside dialog
  val myDialog = dialog containing myButton

 // myButton should be enabled
 // myButton should be disabled
 // myButton should present
 // findAll(myButton) should have size(5)
 // myButton should appear 5 times
 // myButton should not present
 // getTextOfAll( listItem ) should contain(...)


  println(myButton or (span inside div))
  print(!myButton)
  val w2 = button withClass("foo") or (input withClass("bar"))
  println(w2)



  println(element containing(!div))
  // same thing, worded differently
  println(element withProperties(Not(hasDescendant(div))))

  println(element before (div childOf (w2)))

//  div after (childOf(w2))

  println(lastSibling(div))
  withoutClasses("a", "b", "c")
  contains(div, span)

  println((span(4) descendantOf(div withClass("topbar"))))

 println(header withTextContaining ("path") inside body)

  //////////////////actions ////////////////////////////////////////
  dragAndDrop from myButton to w2
  dragAndDrop from myButton toOffset (10, 10)
  click on myButton
  click over  myButton
  hover over myButton
  InBrowser verify !w2
  doubleClick on myButton
  sendKeys("abc", "def") to w2
  sendKeys("abc") toBrowser()
  pressKeyDown(Keys.ALT) inBrowser()
  releaseKey(Keys.ALT) whileFocusedOn w2
}
