package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class Op1Instr extends LirInstr {
    protected LirOperand operand;

    public Op1Instr(Mnemonic mnemonic, LirOperand result, LirOperand operand) {
        super(mnemonic, result);
        this.operand = operand;
    }

    @Override
    public String toString() {
        switch (mnemonic) {
            case RETURN:
                return Logger.format("i{}: return {}", super.id, (operand == null|| operand == LirOperand.illegal) ? "" :
                        operand.toString());
        }
        return Logger.format("i{}: {} {},{}", super.id, mnemonic.name().toLowerCase(), result.toString(),
                operand.toString());

    }
}
