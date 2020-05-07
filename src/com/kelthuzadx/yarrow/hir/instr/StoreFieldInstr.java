package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaField;

public class StoreFieldInstr extends AccessFieldInstr {
    private final HirInstr storeValue;

    public StoreFieldInstr(HirInstr object, int offset, JavaField field, HirInstr storeValue) {
        super(object, offset, field);
        this.storeValue = storeValue;
    }

    public HirInstr getStoreValue() {
        return storeValue;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: i{}.off+{} = i{} [{}.{}]",
                super.id, super.object.id, super.offset, storeValue.id,
                super.field.getDeclaringClass().getUnqualifiedName(), super.field.getName());
    }
}
