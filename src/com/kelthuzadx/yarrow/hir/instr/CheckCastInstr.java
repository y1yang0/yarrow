package com.kelthuzadx.yarrow.hir.instr;

import jdk.vm.ci.meta.JavaType;

public class CheckCastInstr extends Instruction {
    private JavaType klass;
    private Instruction object;

    public CheckCastInstr(JavaType klass, Instruction object) {
        this.klass = klass;
        this.object = object;
    }
}
