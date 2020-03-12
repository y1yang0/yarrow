package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class NewTypeArrayInstr extends Instruction {
    private Instruction len;
    private JavaKind elemType;

    public NewTypeArrayInstr(Instruction len, JavaKind elemType) {
        super(new Value(JavaKind.Object));
        this.len = len;
        this.elemType = elemType;
    }

    @Override
    public String toString() {
        return Logger.f("i{}: new_typearr #{}",super.id,elemType.getJavaName());
    }
}
