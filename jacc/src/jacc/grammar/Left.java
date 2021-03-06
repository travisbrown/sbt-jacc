// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package dev.travisbrown.jacc.grammar;

import dev.travisbrown.jacc.JaccProd;
import java.util.SortedSet;
import java.util.TreeSet;

/** Calculation of left sets.  The left set of a nonterminal symbol X is
 *  the set of all nonterminals Y such that X derives a string of the form
 *  Yw for some sequence of symbols w.  Left sets are used in the
 *  calculation of LR(0) item set closures.
 */
public final class Left extends Analysis {
    private final Grammar  grammar;
    private final int      numNTs;
    private final SortedSet<Integer>[]  left;

    /** Construct a left set analysis for a given grammar.
     */
    public Left(Grammar grammar) {
        super(grammar.getComponents());
        this.grammar  = grammar;
        this.numNTs   = grammar.getNumNTs();
        left          = new SortedSet[numNTs];
        for (int i=0; i<numNTs; i++) {
            left[i] = new TreeSet<>();
            left[i].add(i);
        }
        bottomUp();
    }

    /** Run the analysis at a particular point.  Return a boolean true
     *  if this changed the current approximation at this point.
     */
    protected boolean analyze(int c) {
        boolean changed = false;
        JaccProd[] prods = grammar.getProds(c);
        for (int k=0; k<prods.length; k++) {
            int[] rhs = prods[k].getRhs(this.grammar);
            if (rhs.length>0 && grammar.isNonterminal(rhs[0])) {
                if (left[c].addAll(left[rhs[0]])) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    /** Return a bitset of the left symbols for a given nonterminal.
     */
    public SortedSet<Integer> at(int i) {
        return left[i];
    }

    /** Display the results of the analysis for the purposes of debugging
     *  and inspection.
     */
    public void display(java.io.PrintWriter out) {
        out.println("Left nonterminal sets:");
        for (int i=0; i<left.length; i++) {
            out.print(" Left(" + grammar.getSymbol(i) + "): {");
            out.print(grammar.displaySymbolSet(left[i], 0));
            out.println("}");
        }
    }
}
