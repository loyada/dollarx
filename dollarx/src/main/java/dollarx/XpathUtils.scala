package dollarx


object XpathUtils {

  private def translateTextForPath(txt: String) = s"translate($txt, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"
  def textContains(text: String) = s"contains(${translateTextForPath("text()")}, '${text.toLowerCase()}')"
  def textEquals(text: String) = s"${translateTextForPath("text()")} = '${text.toLowerCase()}'"
  def aggregatedTextEquals(text: String) = s"${translateTextForPath("normalize-space(string(.))")} = '${text.toLowerCase()}'"
  def aggregatedTextContains(text: String) = s"contains(${translateTextForPath("normalize-space(string(.))")}, '${text.toLowerCase()}')"
  val hasSomeText = "string-length(text()) > 0"
  val isHidden = "contains(@style, 'display:none') or contains(normalize-space(@style), 'display: none')"
  def hasClass(className: String) = s"contains(concat(' ', @class, ' '), ' $className ')"
  def hasId(id: String) = s"@id='$id'"
  def hasOneOfClasses(classNames: String*) = classNames.map(hasClass).mkString(" or ")
  def DoesNotExist(path: String) = s"not($path)"
  def DoesNotExistInEntirePage(path: String) = {
    val processedPath = if (path.startsWith("//")) path else "//" + path
    s"/html[not(.$processedPath)]"
  }

}
