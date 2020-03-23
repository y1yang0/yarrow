package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;

public class Op1JumpInstr extends Operand1Instr {
    private Cond condition;
    private BlockStartInstr block;

    public Op1JumpInstr(Opcode opcode, LirOperand result, LirOperand operand) {
        super(opcode, result, operand);
    }
}
