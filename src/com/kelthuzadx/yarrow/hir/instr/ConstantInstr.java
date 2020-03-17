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

            if (value()==null) {
                return Logger.format("i{}: nullptr", super.id);
            } else {
                return Logger.format("i{}: {}", super.id, value());
            }
        }
        return "";
    }
}
