package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaField;

public class StoreFieldInstr extends AccessFieldInstr {
    private Instruction value;

    public StoreFieldInstr(Instruction object, int offset, JavaField field, Instruction value) {
        super(object, offset, field);
        this.value = value;
    }

    @Override
    public String toString() {
        return Logger.f("i{}: store i{}.#{},i{}", super.id, super.object, super.offset, super.field.getDeclaringClass(), value.id);
    }
}
