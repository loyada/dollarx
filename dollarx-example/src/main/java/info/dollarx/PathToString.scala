package info.dollarx

import info.dollarx.ElementProperties._
import info.dollarx.ElementProperties.is._
import info.dollarx.Path._


object PathToString extends App {
  val dialog = div withClass "ui-dialog" withText "foo"
  val myButton = button withClass "foo" that((has textContaining "submit") and (is not hidden)) inside dialog
  val shoppingCart = dialog describedBy "\"shopping cart\" dialog"
  val buyButton = button inside shoppingCart that(has text "buy!")
  println(dialog)
  println(myButton)
  println(shoppingCart withClasses ("foo", "bar"))
  println(buyButton)
}
