package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.hir.instr.HirInstruction;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;

public class Item {
    private boolean destroyRegister;
    private HirInstruction instr;
    private LirOperand result;
    private LirOperand newResult;

    public Item(HirInstruction instr){
        this.instr = instr;
        this.destroyRegister = false;
    }
}
