package info.dollarx.examples

import info.dollarx.{InBrowser, ElementProperties}
import ElementProperties._
import InBrowser._
import org.openqa.selenium.By
import org.openqa.selenium.interactions.HasInputDevices
import org.openqa.selenium.internal.Locatable

import scala.collection.JavaConverters._

object dragAndDropExample1 {
  val fromIndex = 3
  val toIndex = 2

   val rows = (driver findElements By.cssSelector(".ui-dialog .condition")).asScala
   val row = rows(fromIndex)
   val handle = (row findElement By.cssSelector(".handle")).asInstanceOf[Locatable]
   val dest = rows(toIndex).asInstanceOf[Locatable]
   val mouse = driver.asInstanceOf[HasInputDevices].getMouse
   mouse.mouseDown(handle.getCoordinates)
   mouse.mouseMove(dest.getCoordinates)
   mouse.mouseUp(dest.getCoordinates)


   // vs.


   val dialog = has cssClass "ui-dialog"
   val condition = has cssClass "condition" inside dialog
   val myHandle = has cssClass "handle" inside condition(fromIndex)

   InBrowser dragAndDrop  myHandle to condition(toIndex)

}
