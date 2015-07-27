package info.dollarx.examples

import info.dollarx.{InBrowser, ElementProperties, Path}
import org.openqa.selenium.Keys

object AppTest extends App {
  import ElementProperties._
  import InBrowser._
  import Path._
   println(unorderedList that (has cssClass("foo") or  (has.no cssClass("bar"))  and hasText("xxx"), hasChild(listItem)))
  val p2 = has classes("foo", "bar") and hasDescendant(div) and (has(5) children) or (has children)
  println(p2)
  val el: Path = p2
  println(el)
  val menuItem = listItem withClass "setting-value"
  println(span that(withAggregatedTextContaining("danny")) descendantOf menuItem(3) )


  val p3 = (hasClass("foo") or  hasClass("bar")) andNot hasText("xxx")
  val myDiv = Path("div")
  val p31 = has oneOfClasses ("foo", "bar") and  (has.no text("xxx"))
  println(p3)
  println(p31)
  println(p31.toXpath)


  val p4 = hasClass("foo") and isChildOf(Path("//div"))
  println(p4)

  //val p5 = raw("blah and moo xxx") and hasClass("foo") withIndex (4)
//  println(p5)
//  println(p5.getXPath)


  val dialog = div withClass "ui-dialog"
  println(dialog)
  val myButton = button withClass("foo") that(hasTextContaining("submit") or Not(isHidden)) inside dialog



  println(myButton or (span inside div))
  print(!myButton)
  val w2 = button withClass("foo") or (input withClass("bar"))
  println(w2)



  println(element containing(!div))
  // same thing, worded differently
  println(element that(Not(hasDescendant(div))))

  println(element before (div childOf (w2)))

  div after (isChildOf(w2))
  println(last(div))
  withoutClasses("a", "b", "c")
  contains(div, span)

  println((span(4) descendantOf(div withClass("topbar"))))

 println(header withTextContaining ("path") inside body)

}
