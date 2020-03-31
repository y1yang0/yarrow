package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class JavaCastInstr extends Op1Instr {
    private int bytecode;

    public JavaCastInstr(LirOperand result, LirOperand operand, int bytecode) {
        super(Mnemonic.TypeCast, result, operand);
        this.bytecode = bytecode;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: type_cast {},{}", super.id, result.toString(), operand.toString());
    }
}
