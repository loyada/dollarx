package dollarx

import org.openqa.selenium.Keys

object AppTest extends App {
  import ElementProperties._
  import WebEl._
  import InBrowser._
   println(unordered_list withProperties (hasClass("foo") or  Not(hasClass("bar")) and hasText("xxx"), hasChild(list_item)))
  val p2 = hasClass("foo") and hasDescendant(div)
  println(p2)
  val menuItem = list_item withClass ("setting-value")
  println(span withProperties(withAggregatedTextContaining("danny")) descendantOf menuItem(3) )
  val p3 = (hasClass("foo") or  hasClass("bar")) andNot hasText("xxx")
  println(p3)

  val p4 = hasClass("foo") and childOf(WebEl("//div"))
  println(p4)

  val p5 = raw("blah and moo xxx") and hasClass("foo") withIndex (4)
  println(p5)

  val dialog = div withClass "ui-dialog"
  println(dialog)
  val myButton = button withClass("foo") withProperties(hasTextContaining("submit") or Not(isHidden)) inside dialog
  val myDialog = dialog containing myButton

 // myButton must be displayed
 // radioButton should be selected
 // myButton should be enabled
 // myButton should be disabled
 // myButton should existOnPage
 // findAll(myButton) should have size(5)
 // myButton should existOnPage 5 times
 // myButton should not existOnPage
 // getTextOfAll( listItem ) should contain(...)


  println(myButton or (span inside div))
  print(!myButton)
  val w2 = button withClass("foo") or (input withClass("bar"))
  println(w2)



  println(anyElement containing(!div))
  // same thing, worded differently
  println(anyElement withProperties(Not(hasDescendant(div))))

  println(anyElement before (div childOf (w2)))

  println(lastSibling(div))

  println((span(4) descendantOf(div withClass("topbar"))))

 println(anyHeader withTextContaining ("path") inside body)

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
