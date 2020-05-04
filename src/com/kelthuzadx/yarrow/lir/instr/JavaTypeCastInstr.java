package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class JavaTypeCastInstr extends Op1Instr {
    private final int bytecode;

    public JavaTypeCastInstr(LirOperand result, LirOperand operand, int bytecode) {
        super(Mnemonic.TypeCast, result, operand);
        this.bytecode = bytecode;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: cast {},{}", super.id, result.toString(), operand.toString());
    }
}
