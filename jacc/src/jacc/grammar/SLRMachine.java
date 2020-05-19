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
public class SLRMachine extends LookaheadMachine {
    // For convenience, we cache the following fields from grammar:
    private final Follow follow;

    /** Construct a machine for a given grammar.
     */
    public SLRMachine(Grammar grammar) {
        super(grammar);
        this.follow = grammar.getFollow();
        calcLookahead();
    }

    /** Records the lookahead sets for reduce items.  Lookahead sets are
     *  stored in the order specified by Machine.getReducesAt().
     */
    private SortedSet<Integer>[][] laReds;

    /** Return lookahead sets for the reductions at a given state.
     */
    public SortedSet<Integer> getLookaheadAt(int st, int i) {
        return laReds[st][i];
    }

    /** Calculate lookahead sets.
     */
    private void calcLookahead() {
        laReds = new SortedSet[entry.size()][];
        for (int i=0; i<entry.size(); i++) {
            List<Integer> its = new ArrayList<>(getItemsAt(i));
            int[]  rs  = getReducesAt(i);
            laReds[i]  = new SortedSet[rs.length];
            for (int j=0; j<rs.length; j++) {
                int lhs      = items.getItem(its.get(rs[j])).getLhs();
                laReds[i][j] = follow.at(lhs);
            }
        }
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
                    items.getItem(its.get(rs[j])).display(out);
                    out.println();
                    out.print("  Lookahead: {");
                    out.print(grammar.displaySymbolSet(laReds[i][j], numNTs));
                    out.println("}");
                }
            }
        }
    }
}
