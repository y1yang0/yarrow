package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaType;

public class NewInstr extends Instruction {
    JavaType klass;

    public NewInstr(JavaType klass) {
        super(new Value(JavaKind.Object));
        this.klass = klass;
    }

    @Override
    public String toString() {
        return Logger.f("i{}: new #{}",super.id,klass.getName());
    }
}
