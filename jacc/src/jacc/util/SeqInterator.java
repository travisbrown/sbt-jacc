// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package dev.travisbrown.jacc.util;

import java.util.Iterator;

class SeqInterator implements Iterator<Integer> {
    private int count;
    private final int limit;
    public SeqInterator(int count, int limit) {
        this.count = count;
        this.limit = limit;
    }
    public Integer next() {
        return count++;
    }
    public boolean hasNext() {
        return count < limit;
    }
}
