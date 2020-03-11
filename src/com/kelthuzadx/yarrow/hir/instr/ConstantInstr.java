package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;

public class ConstantInstr extends Instruction {
    public ConstantInstr(Value value){
        super(value);
    }
}
