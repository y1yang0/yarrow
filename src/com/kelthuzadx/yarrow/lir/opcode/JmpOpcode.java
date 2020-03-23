package com.kelthuzadx.yarrow.lir.opcode;

import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;

public class JmpOpcode extends Op1Opcode {
    private Cond condition;
    private BlockStartInstr block;

    public JmpOpcode(Mnemonic mnemonic, LirOperand result, LirOperand operand) {
        super(mnemonic, result, operand);
    }
}
