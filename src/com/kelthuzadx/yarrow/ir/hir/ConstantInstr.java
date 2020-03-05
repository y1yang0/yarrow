package com.kelthuzadx.yarrow.ir.hir;

import com.kelthuzadx.yarrow.ir.value.Value;

public class ConstantInstr extends Instruction {
    public ConstantInstr(Value type){
        super.setType(type);
    }
}
