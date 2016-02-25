package com.github.loyada.dollarx

import com.github.loyada.dollarx.RelationOperator.RelationOperator
import com.github.loyada.dollarx.util.{XpathUtils, StringUtil}
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import scala.collection.JavaConverters._
import StringUtil.wrap

object InBrowserFinder {
  def find(driver: WebDriver, el: Path): WebElement = {
    val path = el.getXPath
    try {
      if (el.getUnderlyingSource().isDefined) {
        val underlying = el.getUnderlyingSource().get
        if (path.isDefined) underlying.findElement(By.xpath(path.get)) else underlying
      } else {
        if (el.getXPath.isDefined) {
          val processedPath = processedPathForFind(path.get)
          driver.findElement(By.xpath(processedPath))
        } else {
          throw new IllegalArgumentException("path is empty") // should never happen
        }
      }
    } catch {
      case ex: NoSuchElementException => throw new NoSuchElementException("could not find " + wrap(el), ex)
    }
  }


  def findPageWithout(driver: WebDriver, el: Path): WebElement = {
    if (el.getXPath.isEmpty) {
      throw new UnsupportedOperationException("findPageWithout requires a path")
    }
    val path = el.getXPath.get

    try {
      if (el.getUnderlyingSource().isDefined) {
        val underlying = el.getUnderlyingSource().get
        underlying.findElement(By.xpath("//" + PathOperators.not(el).getXPath.get))
      } else {
        val processedPath = XpathUtils.DoesNotExistInEntirePage(path)
        driver.findElement(By.xpath(processedPath))
      }
    } catch {
      case ex: NoSuchElementException => throw new NoSuchElementException("could not find page without " + wrap(el), ex)
    }
  }

  def findAll(driver: WebDriver, el: Path): List[WebElement] = {
    val path = el.getXPath
    if (el.getUnderlyingSource.isDefined) {
      val underlying = el.getUnderlyingSource().get
      if (path.isDefined) {
        underlying.findElements(By.xpath(path.get)).asScala.toList
      } else {
        List(underlying)
      }
    } else {
      if (path.isDefined) {
        val processedPath = processedPathForFind(path.get)
        driver.findElements(By.xpath(processedPath)).asScala.toList
      } else {
        throw new IllegalArgumentException("path is empty") // should never happen
      }
    }
  }

  def findPageWithNumberOfOccurrences(driver: WebDriver, el: Path, numberOfOccurrences: Int, relationOperator: RelationOperator): WebElement = {
    val path = el.getXPath
    if (path.isEmpty) {
      throw new UnsupportedOperationException("findPageWithNumberOfOccurrences requires a path")
    }
    val opString = RelationOperator.opAsXpathString(relationOperator)
    val pathWithNOccurrences = s"[count(//${path.get})$opString${numberOfOccurrences}]"
    if (el.getUnderlyingSource().isDefined) {
      val underlying = el.getUnderlyingSource().get
      underlying.findElement(By.xpath("." + pathWithNOccurrences))
    } else {
      driver.findElement(By.xpath("/html" + pathWithNOccurrences))
    }
  }

  private def processedPathForFind(path: String): String = {
    if (path.startsWith("not(.//")) {
      String.format("/html[.%s]", path)
    } else if (path.startsWith("not")) {
      val processedPath = path.replaceFirst("not[(]", "not(.//")
      String.format("/html[%s]", processedPath)
    } else {
      val chopn = if (path.startsWith("(") && !path.startsWith("(/")) 1 else 0;
      val prefix = if (path.startsWith("/") || path.startsWith("(/")) "" else
                              if (path.startsWith("(")) "(//" else
                                "//"
      prefix + path.substring(chopn)
    }
  }
}
