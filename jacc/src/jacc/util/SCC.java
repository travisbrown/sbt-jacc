// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package dev.travisbrown.jacc.util;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/** An implementation of the strongly connected components algorithm.
 */
public class SCC {
    public static int[][] get(int[][] depends, int[][] revdeps, int size) {
        return new GetComponents(depends,size,
                 new ArrangeByFinish(revdeps,size)
                     .getFinishOrder())
                   .getComponents();
    }

    public static int[][] get(int[][] depends) {
        return get(depends, invert(depends), depends.length);
    }

    public static int[][] get(int[][] depends, int len) {
        return get(depends, invert(depends, len), len);
    }

    /** A framework for depth first searches.  A search algorithm is
     *  usually described by subclassing, overriding the doneTree and
     *  doneVisit functions as appropriate, and then invoking the search
     *  method.
     */
    private static abstract class DepthFirst {
        private   final Iterator<Integer> seq;
        protected final int[][]   adjs;
        private   final Set<Integer> visited;
        DepthFirst(Iterator<Integer> seq, int[][] adjs) {
            this.seq  = seq;
            this.adjs = adjs;
            visited   = new HashSet<Integer>();
        }

        protected void search() {
            while (seq.hasNext()) {
                if (visit(seq.next())) {
                    doneTree();
                }
            }
        }

        private boolean visit(int i) {
            if (visited.add(i)) {
                int[] adj = adjs[i];
                for (int j=0; j<adj.length; j++) {
                    visit(adj[j]);
                }
                doneVisit(i);
                return true;
            } else {
                return false;
            }
        }

        /** Describes the action to be performed after visiting a particular
         *  node.  The default behavior provided here is to do nothing.
         */
        void doneVisit(int i) {
            // Do nothing
        }

        /** Describes the action to be performed after visiting a particular
         *  tree in the depth first forest.  The default behavior provided
         *  here is to do nothing.
         */
        void doneTree() {
            // Do nothing
        }
    }

    // A Depth-first search that returns an array of index values,
    // arranged in decreasing order of finish time.

    private static class ArrangeByFinish extends DepthFirst {
        private int   dfsNum;
        private int[] order;

        ArrangeByFinish(int [][] dependencies, int size) {
            super(IntStream.range(0,size).iterator(), dependencies);
            dfsNum = size;
            order  = new int[dfsNum];
        }
        void doneVisit(int i) {
            order[--dfsNum] = i;
        }
        int[] getFinishOrder() {
            search();
            return order;
        }
    }

    // A depth first search that builds components

    private static class GetComponents extends DepthFirst {
        private int   numComps;
        private int[] compNo;

        GetComponents(int [][] dependencies, int size, int[] order) {
            super(Arrays.stream(order).iterator(), dependencies);
            numComps = 0;
            compNo   = new int[size];
        }
        void doneVisit(int i) {
            compNo[i] = numComps;
        }
        void doneTree() {
            numComps++;
        }
        int[][] getComponents() {
            search();
            int[] compSize = new int[numComps];
            for (int i=0; i<compNo.length; i++) {
                compSize[compNo[i]]++;
            }
            int[][] comps = new int[numComps][];
            for (int j=0; j<numComps; j++) {
                comps[j] = new int[compSize[j]];
            }
            for (int i=0; i<compNo.length; i++) {
                int j = compNo[i];
                comps[j][--compSize[j]] = i;
            }
            return comps;
        }
    }

    private static int[][] invert(int[][] adj) {
        return invert(adj, adj.length);
    }

    public static int[][] invert(int[][] adj, int len) {
        int[] counts = new int[len];
        for (int i=0; i<len; i++) {
            for (int j=0; j<adj[i].length; j++) {
                counts[adj[i][j]]++;
            }
        }
        int[][] rev = new int[len][];
        for (int i=0; i<len; i++) {
            rev[i] = new int[counts[i]];
        }
        for (int i=0; i<len; i++) {
            for (int j=0; j<adj[i].length; j++) {
                int n = adj[i][j];
                counts[n]--;
                rev[n][counts[n]] = i;
            }
        }
        return rev;
    }

    public static int[][] invert(Map<Integer, int[]> adj) {
        int len = adj.size();

        int[] counts = new int[len];
        for (int i=0; i<len; i++) {
            for (int j=0; j<adj.get(i).length; j++) {
                counts[adj.get(i)[j]]++;
            }
        }
        int[][] rev = new int[len][];
        for (int i=0; i<len; i++) {
            rev[i] = new int[counts[i]];
        }
        for (int i=0; i<len; i++) {
            for (int j=0; j<adj.get(i).length; j++) {
                int n = adj.get(i)[j];
                counts[n]--;
                rev[n][counts[n]] = i;
            }
        }
        return rev;
    }

    private static void displayComponents(PrintWriter out, int[][] comps) {
        out.println("Components (" + comps.length + " in total):");
        for (int i=0; i<comps.length; i++) {
            out.print(" Component " + i + ": {");
            for (int j=0; j<comps[i].length; j++) {
                if (j>0) {
                    out.print(", ");
                }
                out.print(comps[i][j]);
            }
            out.println("}");
        }
    }
}
