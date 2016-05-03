package com.github.loyada.dollarx.util

object XpathUtils {

  private def translateTextForPath(txt: String) = s"translate($txt, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"
  def textContains(text: String) = s"contains(${translateTextForPath("text()")}, '${text.toLowerCase()}')"
  def textEquals(text: String) = s"${translateTextForPath("text()")} = '${text.toLowerCase()}'"
  def aggregatedTextEquals(text: String) = s"${translateTextForPath("normalize-space(string(.))")} = '${text.toLowerCase()}'"
  def aggregatedTextContains(text: String) = s"contains(${translateTextForPath("normalize-space(string(.))")}, '${text.toLowerCase()}')"
  val hasSomeText = "string-length(text()) > 0"
  val isHidden = "contains(@style, 'display:none') or contains(normalize-space(@style), 'display: none')"
  def hasClass(className: String) = s"contains(concat(' ', normalize-space(@class), ' '), ' $className ')"
  def hasId(id: String) = s"@id='$id'"
  def hasOneOfClasses(classNames: String*) = classNames.map(hasClass).mkString(" or ")
  def hasClasses(classNames: String*) = classNames.map(hasClass).mkString(" and ")
  def DoesNotExist(path: String) = s"not($path)"
  def DoesNotExistInEntirePage(path: String) = {
    val processedPath = if (path.startsWith("//")) s".$path"
    else if (path.startsWith("(/")) s"(./${path.substring(2)}"
    else ".//" + path
    s"/html[not($processedPath)]"
  }

  def hasAttribute(attribute: String, value: String): String = {
     String.format("@%s='%s'", attribute, value)
  }

  def hasAttributeName(attrName: String): String = {
    s"attribute::$attrName"
  }


  def insideTopLevel(xpath: String): String = {
    val alreadyInsideTopLevel = xpath.matches("^[(]*[//]+.*")
    val prefix = if (alreadyInsideTopLevel) "" else
                            if ((xpath.startsWith("("))) "(//" else
                                "//"
    val chopN = if ((xpath.startsWith("(") && !alreadyInsideTopLevel)) 1 else 0
    prefix + xpath.substring(chopN)
  }

  def textEndsWith(text: String): String = {
    s"substring(${translateTextForPath("text()")}, string-length(text()) - string-length('$text') +1) = '${text.toLowerCase}'"
  }

  def textStartsWith(text: String): String = s"starts-with(${translateTextForPath("text()")}, '${text.toLowerCase}')"

  def aggregatedTextEndsWith(text: String) = {
    s"substring(${translateTextForPath("normalize-space(string(.))")}, string-length(normalize-space(string(.))) - string-length('$text') +1) = '${text.toLowerCase}'"
  }

  def aggregatedStartsWith(text: String) = s"starts-with(${translateTextForPath("normalize-space(string(.))")}, '${text.toLowerCase}')"
}
