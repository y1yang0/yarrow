package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaField;

public abstract class AccessFieldInstr extends Instruction {
    protected Instruction object;
    protected int offset;
    protected JavaField field;

    public AccessFieldInstr(Instruction object, int offset, JavaField field) {
        super(new Value(field.getJavaKind()));
        this.object = object;
        this.offset = offset;
        this.field = field;
    }
}
