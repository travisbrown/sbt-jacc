// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package dev.travisbrown.jacc.grammar;

import java.util.SortedSet;
import java.util.TreeSet;

/** A machine that provides LR(0) lookahead sets for each reduction.
 *  LR(0) doesn't really provide any lookaheads at all, but we can
 *  still fit it into the LookaheadMachine framework by returning a
 *  set in which all terminal symbols are set.
 */
public class LR0Machine extends LookaheadMachine {
    /** A bitset of all terminal symbols.
     */
    private final SortedSet<Integer> allTokens;

    /** Construct a machine for a given grammar.
     */
    public LR0Machine(Grammar grammar) {
        super(grammar);
        int numTs = grammar.getNumTs();
        allTokens = new TreeSet<>();
        for (int i=0; i<numTs; i++) {
            allTokens.add(i);
        }
    }

    /** Return lookahead sets for the reductions at a given state.
     */
    public SortedSet<Integer> getLookaheadAt(int st, int i) {
        return allTokens;
    }

    /** Output the results of lookahead calculations for
     *  debugging and inspection.
     */
    public void display(java.io.PrintWriter out) {
        super.display(out);
        out.print("Lookahead set is {");
        out.print(grammar.displaySymbolSet(allTokens, numNTs));
        out.println("}");
    }
}
