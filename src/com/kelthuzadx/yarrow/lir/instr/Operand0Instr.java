package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class Operand0Instr extends LirInstr {

    public Operand0Instr(Opcode opcode, LirOperand result) {
        super(opcode, result);
    }

    @Override
    public String toString() {
        return Logger.format("r{}: {} {}", super.id, opcode.name().toLowerCase(), result.toString());

    }
}
