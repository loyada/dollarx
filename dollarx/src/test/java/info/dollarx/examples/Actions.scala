package info.dollarx.examples

import info.dollarx.{ElementProperties, Path, InBrowser}
import org.openqa.selenium.Keys


object Actions extends App {
  import ElementProperties._
  import InBrowser._
  import Path._

  val myButton = button withClass "submit"
  val el = hasChild(div withText "john")

  //////////////////actions ////////////////////////////////////////
  InBrowser dragAndDrop  myButton to el
  InBrowser dragAndDrop myButton to offset (10, 10)
  click on myButton
  click at myButton
  hover over myButton
  InBrowser verify !el
  doubleClick on myButton
  sendKeys("a", "b", "c", "def") to el
  sendKeys("abc") toBrowser()
  pressKeyDown(Keys.ALT) inBrowser()
  releaseKey(Keys.ALT) whileFocusedOn el

}
