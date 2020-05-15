package com.kelthuzadx.yarrow.lir.regalloc;

import java.util.ArrayList;

public class Interval {
    private final ArrayList<int[]> ranges;

    public Interval() {
        this.ranges = new ArrayList<>();
    }

    public void addRange(int from, int to) {
        int[] range = new int[]{from, to};
        ranges.add(range);
    }
}
