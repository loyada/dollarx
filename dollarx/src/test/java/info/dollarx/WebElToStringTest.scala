package info.dollarx

import info.dollarx.ElementProperties._
import info.dollarx.ElementProperties.is._
import Path._


object WebElToStringTest extends App {
  val dialog = div withClass "ui-dialog" withText "foo"
  val myButton = button withClass("foo") that((has textContaining "submit") and (is not hidden)) inside dialog
  val myDialog = dialog describedBy("\"create new entity\" dialog")
  println(myButton)
  println(dialog)
  println(myDialog withClasses ("foo", "bar"))
  println(button  inside myDialog withClasses "foo" withClass "bar")

}
