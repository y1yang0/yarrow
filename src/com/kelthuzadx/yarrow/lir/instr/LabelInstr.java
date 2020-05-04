package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class LabelInstr extends Op0Instr {
    public LabelInstr() {
        super(Mnemonic.LABEL, LirOperand.illegal);
    }

    @Override
    public String toString() {
        return Logger.format("L{}:", super.id);
    }
}
