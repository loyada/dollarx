package info.dollarx

import ElementProperties.{Not, hasTextContaining, isHidden}
import Path._
import info.dollarx.ElementProperties.isHidden


object WebElToStringTest extends App {
  val dialog = div withClass "ui-dialog" withText "foo"
  val myButton = button withClass("foo") that(hasTextContaining("submit") or Not(isHidden)) inside dialog
  val myDialog = dialog describedBy("\"create new entity\" dialog")
  println(dialog)
  println(myDialog withClasses ("foo", "bar"))
  println(button  inside myDialog withClasses "foo" withClass "bar")

}
