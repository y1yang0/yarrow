package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaType;

public class NewObjectArrayInstr extends Instruction {
    private Instruction len;
    private JavaType klass;

    public NewObjectArrayInstr(Instruction len, JavaType klass) {
        super(new Value(JavaKind.Object));
        this.len = len;
        this.klass = klass;
    }

    @Override
    public String toString() {
        return Logger.f("i{}: new_objarr #{}", super.id, klass.getName());
    }
}
