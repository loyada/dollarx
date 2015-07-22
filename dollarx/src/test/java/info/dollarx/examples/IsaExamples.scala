package info.dollarx.examples

import dollarx.InBrowser
import info.dollarx.{InBrowser, ElementProperties, Path}

/**
 * Created by danny on 6/12/2015.
 */
object IsaExamples extends App{
  import ElementProperties._
  import Path._
  import InBrowser._

  var el: Path = is after(div)
  println(el)
  println(el.getXPath().get)
}
