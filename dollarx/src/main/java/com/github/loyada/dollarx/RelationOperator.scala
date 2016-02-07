package com.github.loyada.dollarx

object RelationOperator extends Enumeration {
  type RelationOperator = Value
  val exactly, orMore, orLess = Value
  def opAsXpathString(relationOperator: RelationOperator) = {
    val sByOp = Map(exactly -> "=", orMore -> ">=", orLess -> "<=")
    sByOp(relationOperator)
  }
  def opAsEnglish(relationOperator: RelationOperator) = {
    val sByOp = Map(exactly -> " ", orMore -> " at least ", orLess -> " at most ")
    sByOp(relationOperator)
  }
}