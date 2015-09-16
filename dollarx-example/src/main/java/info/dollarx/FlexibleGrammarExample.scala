package info.dollarx


import info.dollarx.ElementProperties._
import info.dollarx.Path._


object FlexibleGrammarExample extends App{
  val dialog = div withClass "ui-dialog"
  val row = has cssClass "condition" inside dialog

  // or...
  has cssClass "condition" and (is inside dialog)
  has cssClass "condition" and (is containedIn dialog)
  has cssClass "condition" and (is descendantOf dialog)
  has cssClass "condition" and (has ancestor dialog)

  element inside dialog  withClass "condition"
  is inside dialog withClass "condition"
  element withClass "condition" inside dialog

  element withClass "condition" descendantOf dialog
  element that(has cssClass "condition") inside dialog
  element that(has cssClass "condition", has ancestor dialog)
  element that((has cssClass "condition") and (has ancestor dialog))
}
