package dollarx

object AppTest extends App {
  import ElementProperties._
  import WebEl._
  import InBrowser._
   println(unordered_list withProperties (hasClass("foo") or  Not(hasClass("bar")) and hasText("xxx"), hasChild(list_item)))
  val p2 = hasClass("foo") and hasDecendant(div)
  println(p2)
  val menuItem = list_item withClass ("setting-value")
  println(span withProperties(withAggregatedTextContaining("danny")) descendantOf menuItem(3) )
  val p3 = (hasClass("foo") or  hasClass("bar")) andNot hasText("xxx")
  println(p3)

  val p4 = hasClass("foo") and childOf(WebEl("//div"))
  println(p4)

  val p5 = raw("blah and moo xxx") and hasClass("foo") withIndex (4)
  println(p5)

  val dialog = div withClass("ui-dialog")

  val myButton = button withClass("foo") withProperties(hasTextContaining("submit") or Not(isHidden)) inside dialog
  click myButton
 // InBrowser hoverOver myButton
  verify w2

  myButton should be visible

  println(myButton or (span inside div))

  val w2 = button withClass("foo") or (input withClass("bar"))
  println(w2)

  println(anyElement containing(!div))
  // same thing, worded differently
  println(anyElement withProperties(Not(hasDecendant(div))))

  println((anyElement before (div childOf(w2))))

  println(lastSibling(div))

  println((span(4) descendantOf(div withClass("topbar"))))

 println(anyHeader withTextContaining ("path") inside body)
  header1
}
