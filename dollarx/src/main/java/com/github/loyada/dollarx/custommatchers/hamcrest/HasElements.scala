package com.github.loyada.dollarx.custommatchers.hamcrest

import com.github.loyada.dollarx.Path

case class HasElements(path: Path) {
  def present(nTimes: Int): HasElementNTimes = {
     new HasElementNTimes(path, nTimes)
  }
}
