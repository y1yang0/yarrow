package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaField;

public abstract class AccessFieldInstr extends HirInstruction {
    protected HirInstruction object;
    protected int offset;
    protected JavaField field;

    public AccessFieldInstr(HirInstruction object, int offset, JavaField field) {
        super(new Value(field.getJavaKind()));
        this.object = object;
        this.offset = offset;
        this.field = field;
    }

    public HirInstruction getObject() {
        return object;
    }

    public int getOffset() {
        return offset;
    }

    public JavaField getField() {
        return field;
    }
}
