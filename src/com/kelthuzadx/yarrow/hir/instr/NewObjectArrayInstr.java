package com.kelthuzadx.yarrow.hir.instr;

import jdk.vm.ci.meta.JavaType;

public class NewObjectArrayInstr extends Instruction {
    private Instruction len;
    private JavaType klass;

    public NewObjectArrayInstr(Instruction len, JavaType klass) {
        this.len = len;
        this.klass = klass;
    }

}
