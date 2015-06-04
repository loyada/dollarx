package dollarx.examples

import dollarx.InBrowser._
import org.openqa.selenium.By

import scala.collection.JavaConverters._

object ClickExamples {

  val rows = (driver findElements By.cssSelector("div.ui-dialog .condition")).asScala
  val exprs = (rows(2) findElements By.cssSelector(".binary-expr")).asScala
  val removeButton1 = exprs(3) findElement By.cssSelector("button.remove-expr")
  removeButton1.click()
  //issues:
  //1. Inefficient.
  //2. Race conditions / stale elements.
  //3. Error message - not clear.
  //4. Readability not great.

  val removeButton2 = driver findElement By.xpath("((//*[contains(concat(' ', @class, ' '), ' ui-dialog ')]" +
   "//*[contains(concat(' ', @class, ' '), ' condition ')])[3]//" +
   "//*[contains(concat(' ', @class, ' '), ' binary-expression ')])[4]//button" +
  "[contains(concat(' ', @class, ' '), ' remove-expr ')]")
   removeButton2.click()
  //   // issues:
  //  //  1. Difficult to write, and more so to read.
  //  //  2. Brittle.
  //  //

}
