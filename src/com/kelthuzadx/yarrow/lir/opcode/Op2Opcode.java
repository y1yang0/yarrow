package com.kelthuzadx.yarrow.lir.opcode;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class Op2Opcode extends LirOpcode {
    private LirOperand leftOperand;
    private LirOperand rightOperand;

    public Op2Opcode(Mnemonic mnemonic, LirOperand result, LirOperand leftOperand, LirOperand rightOperand) {
        super(mnemonic, result);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public String toString() {
        return Logger.format("r{}: {} {} {},{}", super.id, mnemonic.name().toLowerCase(), result.toString(),
                leftOperand.toString(), rightOperand.toString());

    }
}
