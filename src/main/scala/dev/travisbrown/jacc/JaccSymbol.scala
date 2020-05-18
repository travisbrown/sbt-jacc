package dev.travisbrown.jacc

/** Represents a symbol in a jacc grammar.
 */
case class JaccSymbol(name: String, num: Int, tokenNo: Int, fixity: Option[Fixity], tpe: Option[String], prods: Array[JaccProd]) {
  def getType: String = tpe.orNull

  override def toString(): String = name
}