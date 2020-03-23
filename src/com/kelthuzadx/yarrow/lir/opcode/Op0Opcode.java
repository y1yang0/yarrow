package com.kelthuzadx.yarrow.lir.opcode;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class Op0Opcode extends LirOpcode {

    public Op0Opcode(Mnemonic mnemonic, LirOperand result) {
        super(mnemonic, result);
    }

    @Override
    public String toString() {
        return Logger.format("r{}: {} {}", super.id, mnemonic.name().toLowerCase(), result.toString());

    }
}
