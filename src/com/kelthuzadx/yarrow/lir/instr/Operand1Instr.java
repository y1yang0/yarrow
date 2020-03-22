package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.lir.operand.OperandType;
import com.kelthuzadx.yarrow.util.Logger;

public class Operand1Instr extends LirInstr {
    private OperandType type;
    private LirOperand operand;

    public Operand1Instr(Opcode opcode, LirOperand result,LirOperand operand) {
        super(opcode, result);
        this.type = operand.type();
        this.operand = operand;
    }

    @Override
    public String toString() {
        return Logger.format("{}: {} {}",result.toString(),opcode.name().toLowerCase(),
                operand.toString());

    }
}
