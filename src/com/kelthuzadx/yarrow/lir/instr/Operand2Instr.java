package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class Operand2Instr extends LirInstr {
    private LirOperand leftOperand;
    private LirOperand rightOperand;

    public Operand2Instr(Opcode opcode, LirOperand result, LirOperand leftOperand, LirOperand rightOperand) {
        super(opcode, result);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public String toString() {
        return Logger.format("r{}: {} {} {},{}", super.id, opcode.name().toLowerCase(), result.toString(),
                leftOperand.toString(), rightOperand.toString());

    }
}
