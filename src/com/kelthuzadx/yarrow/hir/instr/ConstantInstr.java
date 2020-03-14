package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class ConstantInstr extends Instruction {
    public ConstantInstr(Value value) {
        super(value);
    }


    @Override
    public String toString() {
        if (!isType(JavaKind.Illegal)) {
            if (getValue().isEmpty() && isType(JavaKind.Object)) {
                return Logger.format("i{}: const null", super.id);
            } else {
                return Logger.format("i{}: const {}", super.id, getValue().get());
            }
        }
        return "";
    }
}
