package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.lir.operand.OperandType;
import com.kelthuzadx.yarrow.util.Logger;

public class Operand2Instr extends LirInstr {
    private OperandType type;
    private LirOperand leftOperand;
    private LirOperand rightOperand;

    public Operand2Instr(Opcode opcode, LirOperand result, LirOperand leftOperand, LirOperand rightOperand) {
        super(opcode, result);
        this.type = leftOperand.type();
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public String toString() {
        return Logger.format("x{}: {}",super.id,opcode.name());

    }
}
