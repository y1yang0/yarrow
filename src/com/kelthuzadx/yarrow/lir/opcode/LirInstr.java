package com.kelthuzadx.yarrow.lir.opcode;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Increment;

public class LirInstr {
    protected int id;
    protected Mnemonic mnemonic;
    protected LirOperand result;

    public LirInstr(Mnemonic mnemonic, LirOperand result) {
        this.id = Increment.next(LirInstr.class);
        this.mnemonic = mnemonic;
        this.result = result;
    }
}
