package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class Op1TypeCastInstr extends Operand1Instr {
    private int bytecode;

    public Op1TypeCastInstr(LirOperand result, LirOperand operand, int bytecode) {
        super(Opcode.TypeCast, result, operand);
        this.bytecode = bytecode;
    }

    @Override
    public String toString() {
        return Logger.format("r{}: {} {} {}", super.id, Bytecode.forName(bytecode).toLowerCase(), result.toString(), operand.toString());
    }
}
