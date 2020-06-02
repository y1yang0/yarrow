package com.kelthuzadx.yarrow.lir.regalloc;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class Interval {
    private final ArrayList<int[]> ranges;
    private final ArrayList<Integer> usePositions;

    public Interval() {
        this.ranges = new ArrayList<>();
        this.usePositions = new ArrayList<>();
    }

    public void addRange(int from, int to) {
        int[] range = new int[]{from, to};
        ranges.add(range);
    }

    public void addUsePosition(int usePosition) {
        usePositions.add(usePosition);
    }

    public void changeFirstRange(int from) {
        ranges.get(0)[0] = from;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        SortedSet<Integer> pos = new TreeSet<>();
        for (int[] range : ranges) {
            pos.add(range[0]);
            pos.add(range[1]);
        }
        sb.append(pos.first());
        for (int i = pos.first(); i < pos.last(); i++) {
            if (pos.contains(i)) {
                sb.append("|");
            } else {
                sb.append("——");
            }
        }
        sb.append(pos.last());
        return sb.toString();
    }
}
