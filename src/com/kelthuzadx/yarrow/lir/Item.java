package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.hir.instr.HirInstr;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;

public class Item {
    private boolean destroyRegister;
    private HirInstr instr;
    private LirOperand result;
    private LirOperand newResult;

    public Item(HirInstr instr) {
        this.instr = instr;
        this.destroyRegister = false;
    }
}
