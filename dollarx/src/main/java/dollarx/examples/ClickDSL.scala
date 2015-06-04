package dollarx.examples

import dollarx.ElementProperties.has
import dollarx.InBrowser.click
import dollarx.WebEl._

object ClickDSL extends App {

  val dialog = div withClass "ui-dialog"
  val row = has cssClass "condition" inside dialog
  // or ... inside dialog and has cssClass "condition"
  // or ... element withClass "condition" inside dialog
  // or ... element withClass "condition" descendantOf dialog
  // or ... element withProperties (hasClass("condition")) inside dialog
  // or ... element withProperties (hasClass("condition", hasAncestor(dialog))
  // or ... element withProperties ((has cssClass "condition") and hasAncestor(dialog))
  // or ...

  val expression = has cssClass "binary-expr" inside row(2)
  val removeButton = button withClass "remove-expr" inside expression(3)
  println(row withText("John"))
  click on removeButton

  //Note:
  //1. Very readable.  Resembles English.
  //2. Grammar is flexible.
  //3. Logging is readable!. Try println(row withText("John"))
  //4. iMnimal interaction with browser - optimized, no
  //
}
