package dollarx.examples

import dollarx.ElementProperties.has
import dollarx.InBrowser.click
import dollarx.Path$
import dollarx.Path._

object ClickDSL extends App {

  val dialog = div withClass "ui-dialog"
  val row = has cssClass "condition"  inside dialog
  val expression = has cssClass "binary-expr" inside row(2)
  val removeButton = button withClass "remove-expr" inside expression(3)
  click on removeButton

  //Note:
  //1. Very readable.  Resembles English.
  //2. Grammar is flexible.
  //3. Logging is readable!. Try println(row withText("John")) below
  //4. minimal interaction with browser - optimized
}
