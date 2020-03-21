package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaField;

public class StoreFieldInstr extends AccessFieldInstr {
    private HirInstruction value;

    public StoreFieldInstr(HirInstruction object, int offset, JavaField field, HirInstruction value) {
        super(object, offset, field);
        this.value = value;
    }


    @Override
    public String toString() {
        return Logger.format("i{}: i{}.off+{} = i{} [{}.{}]",
                super.id, super.object.id, super.offset, value.id,
                super.field.getDeclaringClass().getUnqualifiedName(), super.field.getName());
    }
}
