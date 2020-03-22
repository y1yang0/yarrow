package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class Operand1Instr extends LirInstr {
    protected LirOperand operand;

    public Operand1Instr(Opcode opcode, LirOperand result, LirOperand operand) {
        super(opcode, result);
        this.operand = operand;
    }

    @Override
    public String toString() {
        return Logger.format("r{}: {} {} {}", super.id, opcode.name().toLowerCase(), result.toString(),
                operand.toString());

    }
}
