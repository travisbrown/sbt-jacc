// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
//

package dev.travisbrown.jacc;

import dev.travisbrown.jacc.grammar.LookaheadMachine
import dev.travisbrown.jacc.grammar.Resolver
import dev.travisbrown.jacc.grammar.Tables

/** Describes the strategy for resolving conflicts in jacc generated parsers.
 *
 *  Construct a conflict resolver for a given machine, following the
 *  rules and conventions of Jacc/yacc.
 */
class JaccResolver(machine: LookaheadMachine) extends Resolver {
  private val conflicts: Array[Conflicts] = new Array[Conflicts](machine.getNumStates)
  private var numSRConflicts = 0
  private var numRRConflicts = 0

  /** Return the number of shift/reduce conflicts detected.
   */
  def getNumSRConflicts(): Int = this.numSRConflicts

  /** Return the number of reduce/reduce conflicts detected.
   */
  def getNumRRConflicts(): Int = this.numRRConflicts

  /** Returns a description of the conflicts at a given state.
   */
  def getConflictsAt(st: Int): String = Conflicts.describe(machine, st, conflicts(st))

  /** Resolve a shift/reduce conflict.  First, see if the conflict
   *  can be resolved using fixity information.  If that fails, we
   *  choose the shift over the reduce and report a conflict.
   */
  def srResolve(tables: Tables, st: Int, tok: Int, redNo: Int): Unit = {
    val grammar = machine.getGrammar()
    val sym = grammar.getTerminal(tok)
    val its = machine.getItemsAt(st)
    val items = machine.getItems()
    val prod = items.getItem(its.at(redNo)).getProd()

    (sym, prod) match {
      case (jSym: JaccSymbol, jProd: JaccProd) =>
        if (jSym.getFixity.eq(null) || jProd.getFixity.eq(null)) {
          conflicts(st) = Conflicts.sr(tables.getArgAt(st)(tok), redNo, sym, conflicts(st))
          numSRConflicts += 1
          return
        }
        Fixity.order.tryCompare(jProd.getFixity, jSym.getFixity) match {
          case Some(x) if x > 0 => tables.setReduce(st, tok, redNo)
          case Some(_)          => ()
          case None =>
            conflicts(st) = Conflicts.sr(tables.getArgAt(st)(tok), redNo, sym, conflicts(st))
            numSRConflicts += 1
        }
      case (_, _) =>
    }
  }

  /** Resolve a reduce/reduce conflict.  We cannot ever avoid a
   *  reduce/reduce conflict, but the entry that we leave in the
   *  table must be for the production with the lowest number.
   */
  def rrResolve(tables: Tables, st: Int, tok: Int, redNo: Int): Unit = {
    val grammar = machine.getGrammar()
    val redNo0 = tables.getArgAt(st)(tok)
    val its = machine.getItemsAt(st)
    val items = machine.getItems()
    val prod0 = items.getItem(its.at(redNo0)).getProd()
    val prod = items.getItem(its.at(redNo)).getProd()
    val sym = grammar.getTerminal(tok)

    if (prod.getSeqNo < prod0.getSeqNo) {
      tables.setReduce(st, tok, redNo)
    }

    conflicts(st) = Conflicts.rr(redNo0, redNo, sym, conflicts(st))
    numRRConflicts += 1;
  }
}
