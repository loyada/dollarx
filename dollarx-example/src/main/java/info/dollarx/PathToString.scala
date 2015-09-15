package info.dollarx

import info.dollarx.ElementProperties._
import info.dollarx.ElementProperties.is._
import info.dollarx.ElementProperties.has.not._

import info.dollarx.Path._


object PathToString extends App {
  val dialog = div withClass "ui-dialog" withText "foo"
  val myButton = button withClass "foo" that((has textContaining "submit") and (is not hidden)) inside dialog
  val shoppingCart = dialog describedBy "\"shopping cart\" dialog"
  val buyButton = button inside shoppingCart that(has text "buy!")

  println(dialog)
  // div, that has class "ui-dialog", and has the text "foo"

  println(myButton)
  // button, that has class "foo", and (has text containing "submit" and not (is hidden)), inside (div, that has class "ui-dialog", and has the text "foo")

  println(shoppingCart withClasses ("foo", "bar"))
  // "shopping cart" dialog, that has classes [foo, bar]

  println(buyButton)
  // button, inside ("shopping cart" dialog), and has the text "buy!"


  // has-no, or
  println(div that( has no cssClass("foo", "bar")) or (span withClass "moo"))
  //(div, that has non of the classes: [foo, bar]) or (span, that has class "moo")

  //anything but
  val name = listItem withClass "first-name" describedBy "first name entry"
  println(!(name that(has text "Danny")))
  // anything except (first name entry, that has the text "Danny")

}
