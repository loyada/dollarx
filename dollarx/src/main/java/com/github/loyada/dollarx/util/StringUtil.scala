package com.github.loyada.dollarx.util

import com.github.loyada.dollarx.Path

object StringUtil {
  def wrap(el: Path): String = {
    val asString: String = el.toString
    return if ((asString.contains(" "))) String.format("(%s)", asString) else asString
  }
}
