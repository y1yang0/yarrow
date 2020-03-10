package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.value.Value;
import jdk.vm.ci.meta.JavaField;

public class StoreFieldInstr extends AccessFieldInstr {
    private Instruction value;

    public StoreFieldInstr(Instruction object, int offset, JavaField field, Instruction value) {
        super(object, offset, field);
        this.value = value;
    }
}
