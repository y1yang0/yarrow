package com.kelthuzadx.yarrow.hir.instr;

import jdk.vm.ci.meta.JavaType;

public class MultiNewArrayInstr extends Instruction {
    private JavaType klass;
    private Instruction[] dimenInstrs;

    public MultiNewArrayInstr(JavaType klass, Instruction[] dimenInstrs) {
        this.klass = klass;
        this.dimenInstrs = dimenInstrs;
    }
}
