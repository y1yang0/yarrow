package com.kelthuzadx.yarrow.hir.instr;

import jdk.vm.ci.meta.JavaType;

public class InstanceOfInstr extends Instruction {
    private JavaType klass;
    private Instruction object;

    public InstanceOfInstr(JavaType klass, Instruction object) {
        this.klass = klass;
        this.object = object;
    }
}
