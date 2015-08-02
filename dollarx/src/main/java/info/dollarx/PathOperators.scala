package info.dollarx


object PathOperators {

  def not(path: Path): Path = {
    if (path.getXPath.isEmpty) throw new IllegalArgumentException()
    val pathString = path.getXPath.get
    if (pathString.contains("::") || pathString.contains("/")) throw new UnsupportedOperationException("operator \"not\" is not supported on a path with elements relation")
    new Path(xpath = Some(s"*[not(self::$pathString)]"),
      underlyingSource = path.getUnderlyingSource,
      xpathExplanation = Some(String.format("anything except (%s)", path)))
  }
}
