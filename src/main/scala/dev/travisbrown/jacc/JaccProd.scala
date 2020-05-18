package dev.travisbrown.jacc

import dev.travisbrown.jacc.grammar.Grammar

case class JaccProd(prodSyms: Array[String], seqNo: Int, action: Option[String]) {
  def getRhs(grammar: Grammar): Array[Int] = prodSyms.map(name => grammar.lookup(name).tokenNo)

  def getLabel: String = seqNo.toString

  def fixity(grammar: Grammar): Option[Fixity] = prodSyms.map(grammar.lookup).reverse.collectFirst {
    case JaccSymbol(_, _, _, Some(f), _, _) => f
  }

  def getAction: String = action.orNull
}
