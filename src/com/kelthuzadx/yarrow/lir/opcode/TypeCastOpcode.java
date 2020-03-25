package com.kelthuzadx.yarrow.lir.opcode;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class TypeCastOpcode extends Op1Opcode {
    private int bytecode;

    public TypeCastOpcode(LirOperand result, LirOperand operand, int bytecode) {
        super(Mnemonic.TypeCast, result, operand);
        this.bytecode = bytecode;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: {} {} {}", super.id, Bytecode.forName(bytecode).toLowerCase(), result.toString(), operand.toString());
    }
}
