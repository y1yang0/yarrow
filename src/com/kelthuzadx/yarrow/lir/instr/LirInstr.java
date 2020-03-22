package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;

public class LirInstr {
    private Opcode opcode;
    private LirOperand result;

    public LirInstr(Opcode opcode, LirOperand result) {
        this.opcode = opcode;
        this.result = result;
    }
}
