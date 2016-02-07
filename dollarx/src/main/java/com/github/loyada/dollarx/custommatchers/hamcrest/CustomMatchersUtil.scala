package com.github.loyada.dollarx.custommatchers.hamcrest

import com.github.loyada.dollarx.Path

object CustomMatchersUtil {
  def wrap(el: Path): String = {
    val asString: String = el.toString
    return if ((asString.contains(" "))) String.format("(%s)", asString) else asString
  }
}
