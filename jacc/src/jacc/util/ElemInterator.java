// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package dev.travisbrown.jacc.util;

import java.util.Iterator;

class ElemInterator implements Iterator<Integer> {
    private int count;
    private final int limit;
    private final int a[];
    public ElemInterator(int[] a, int lo, int hi) {
        this.a     = a;
        this.count = lo;
        this.limit = hi;
    }
    public ElemInterator(int[] a) {
        this (a, 0, a.length);
    }
    public Integer next() {
        return a[count++];
    }
    public boolean hasNext() {
        return count < limit;
    }
}

