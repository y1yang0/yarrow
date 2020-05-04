package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class Op2Instr extends LirInstr {
    private final Cond cond;
    private final LirOperand leftOperand;
    private final LirOperand rightOperand;

    public Op2Instr(Mnemonic mnemonic, LirOperand result, LirOperand leftOperand, LirOperand rightOperand) {
        super(mnemonic, result);
        this.cond = null;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public Op2Instr(Mnemonic mnemonic, Cond cond, LirOperand result, LirOperand leftOperand, LirOperand rightOperand) {
        super(mnemonic, result);
        this.cond = cond;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: {} {} {},{}", super.id, mnemonic.name().toLowerCase(), result.toString(),
                leftOperand.toString(), rightOperand.toString());

    }
}
