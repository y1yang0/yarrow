package com.kelthuzadx.yarrow.lir.opcode;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class Op1Opcode extends LirOpcode {
    protected LirOperand operand;

    public Op1Opcode(Mnemonic mnemonic, LirOperand result, LirOperand operand) {
        super(mnemonic, result);
        this.operand = operand;
    }

    @Override
    public String toString() {
        return Logger.format("r{}: {} {} {}", super.id, mnemonic.name().toLowerCase(), result.toString(),
                operand.toString());

    }
}
