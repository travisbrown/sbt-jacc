// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package dev.travisbrown.jacc.grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/** A machine that provides SLR lookahead sets for each reduction.
 */
public final class SLRMachine extends LookaheadMachine {
    /** Records the lookahead sets for reduce items.  Lookahead sets are
     *  stored in the order specified by Machine.getReducesAt().
     */
    private final SortedSet<Integer>[][] laReds;

    /** Construct a machine for a given grammar.
     */
    public SLRMachine(Grammar grammar) {
        super(grammar);

        /** Calculate lookahead sets.
         */
        this.laReds = new SortedSet[entry.size()][];

        for (int i=0; i<entry.size(); i++) {
            List<Integer> its = new ArrayList<>(this.getItemsAt(i));
            int[]  rs  = getReducesAt(i);
            this.laReds[i]  = new SortedSet[rs.length];
            for (int j=0; j<rs.length; j++) {
                int lhs      = this.getItems().getItem(its.get(rs[j])).getLhs();
                laReds[i][j] = this.getGrammar().getFollow().at(lhs);
            }
        }
    }

    /** Return lookahead sets for the reductions at a given state.
     */
    public SortedSet<Integer> getLookaheadAt(int st, int i) {
        return this.laReds[st][i];
    }

    /** Output the results of lookahead calculations for
     *  debugging and inspection.
     */
    public void display(java.io.PrintWriter out) {
        super.display(out);
        for (int i=0; i<entry.size(); i++) {
            List<Integer> its = new ArrayList<>(getItemsAt(i));
            int[]  rs  = getReducesAt(i);
            if (rs.length>0) {
                out.println("In state " + i + ":");
                for (int j=0; j<rs.length; j++) {
                    out.print(" Item: ");
                    this.getItems().getItem(its.get(rs[j])).display(out);
                    out.println();
                    out.print("  Lookahead: {");
                    out.print(this.getGrammar().displaySymbolSet(this.laReds[i][j], this.getGrammar().getNumNTs()));
                    out.println("}");
                }
            }
        }
    }
}
