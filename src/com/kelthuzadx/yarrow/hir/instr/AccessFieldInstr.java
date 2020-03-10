package com.kelthuzadx.yarrow.hir.instr;

import jdk.vm.ci.meta.JavaField;

public class AccessFieldInstr extends Instruction {
    private Instruction object;
    private int offset;
    private JavaField field;

    public AccessFieldInstr(Instruction object, int offset, JavaField field) {
        this.object = object;
        this.offset = offset;
        this.field = field;
    }
}
