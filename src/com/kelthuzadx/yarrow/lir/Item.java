package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.hir.instr.HirInstruction;
import com.kelthuzadx.yarrow.lir.instr.LirOperand;
import com.kelthuzadx.yarrow.lir.instr.OperandType;

import java.io.InputStream;

public class Item {
    private boolean destroyRegister;
    private HirInstruction instr;
    private LirOperand result;
    private LirOperand newResult;

    public Item(HirInstruction instr){
        this.instr = instr;
        this.destroyRegister = false;
        this.result = LirOperand.createIllegal();
    }
}
