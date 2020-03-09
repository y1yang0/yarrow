package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.value.Value;

public class ConstantInstr extends Instruction {
    public ConstantInstr(Value type){
        super.setValue(type);
    }
}
