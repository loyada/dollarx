package info.dollarx.singlebrowser

import org.openqa.selenium.WebDriver
import org.openqa.selenium.NoSuchElementException

import info.dollarx.{Browser, Path}

object SingleBrowser {
  var singleDriver: WebDriver = _

  def apply(): SingleBrowser = new SingleBrowser


  object Predicates {
    def apearsNTimes(el: Path, n: Int): Boolean = {
      try {
        SingleBrowser().findAll(el).size == n
      } catch {
        case _: NoSuchElementException => n==0
      }
    }

    def isPresent(path: Path): Boolean = {
      try {
        SingleBrowser().find(path)
        true
      } catch {
        case _: NoSuchElementException => false
      }
    }

    def isEnabled(path: Path): Boolean = {
      SingleBrowser().find(path).isEnabled
    }

    def isSelected(path: Path): Boolean = {
      SingleBrowser().find(path).isSelected
    }

    def isDisplayed(path: Path): Boolean = {
      SingleBrowser().find(path).isDisplayed
    }
  }
}

class SingleBrowser extends Browser{
  override protected var driver: WebDriver = SingleBrowser.singleDriver
}



