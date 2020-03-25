package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaField;

public abstract class AccessFieldInstr extends HirInstr {
    protected HirInstr object;
    protected int offset;
    protected JavaField field;

    public AccessFieldInstr(HirInstr object, int offset, JavaField field) {
        super(field.getJavaKind());
        this.object = object;
        this.offset = offset;
        this.field = field;
    }

    public HirInstr getObject() {
        return object;
    }

    public int getOffset() {
        return offset;
    }

    public JavaField getField() {
        return field;
    }
}
