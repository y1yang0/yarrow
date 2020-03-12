package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaField;

public class LoadFieldInstr extends AccessFieldInstr {
    public LoadFieldInstr(Instruction object, int offset, JavaField field) {
        super(object, offset, field);
    }

    @Override
    public String toString() {
        return Logger.f("i{}: load i{}.#{}", super.id, super.object, super.offset, super.field.getDeclaringClass());
    }
}
