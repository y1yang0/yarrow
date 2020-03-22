package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.lir.operand.OperandType;

public class Operand1Instr extends LirInstr {
    private OperandType type;
    private LirOperand operand;

    public Operand1Instr(Opcode opcode, LirOperand result, OperandType type, LirOperand operand) {
        super(opcode, result);
        this.type = type;
        this.operand = operand;
    }
}
