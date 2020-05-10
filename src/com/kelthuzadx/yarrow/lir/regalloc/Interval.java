package com.kelthuzadx.yarrow.lir.regalloc;

import jdk.vm.ci.meta.JavaKind;

public class Interval {
    private final int virtualRegisterNum;
    private int physicalRegisterNum;
    private final JavaKind type;

    private int from;  //inclusive
    private int to;    // exclusive

    public Interval(int virtualRegisterNum, JavaKind type) {
        this.virtualRegisterNum = virtualRegisterNum;
        this.type = type;
    }
}
