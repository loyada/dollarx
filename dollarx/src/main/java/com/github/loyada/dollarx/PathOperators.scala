package com.github.loyada.dollarx


object PathOperators {

  def not(path: Path): Path = {
    if (path.getXPath.isEmpty) throw new IllegalArgumentException()
    new Path(xpath = Some(s"*[not(self::${ElementPropertiesHelper.transformXpathToCorrectAxis(path).get})]"),
      underlyingSource = path.getUnderlyingSource,
      xpathExplanation = Some(String.format("anything except (%s)", path)),
      alternateXpath = Some(s"*[not(self::${path.getAlternateXPath})]"))
  }
}
