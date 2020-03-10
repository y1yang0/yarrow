package com.kelthuzadx.yarrow.hir.instr;

import jdk.vm.ci.meta.JavaField;

public class LoadFieldInstr extends AccessFieldInstr {
    public LoadFieldInstr(Instruction object, int offset, JavaField field) {
        super(object, offset, field);
    }
}
