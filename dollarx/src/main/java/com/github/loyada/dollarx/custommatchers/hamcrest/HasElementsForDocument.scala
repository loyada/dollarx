package com.github.loyada.dollarx.custommatchers.hamcrest

import com.github.loyada.dollarx.Path


case class HasElementsForDocument(path: Path) {
  def present(nTimes: Int): HasElementNTimesForDocument = {
    new HasElementNTimesForDocument(path, nTimes)
  }

}
