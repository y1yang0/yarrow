package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.value.Value;
import com.kelthuzadx.yarrow.hir.value.ValueType;
import jdk.vm.ci.meta.JavaType;

public class NewInstr extends Instruction {
    JavaType klass;

    public NewInstr(JavaType klass) {
        super(new Value(ValueType.Object,null));
        this.klass = klass;
    }
}
