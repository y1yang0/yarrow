package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.hir.instr.ConstantInstr;

public class LirOperandFactory {
    public static LirOperand createConstInt(ConstantInstr instr){
        return new LirOperand(instr.value());
    }
}
