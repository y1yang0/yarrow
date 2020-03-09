package com.kelthuzadx.yarrow.hir.instr;

public class LogicInstr extends Op2Instr {
    public LogicInstr(int opcode, Instruction left, Instruction right) {
        super(opcode, left, right);
    }
}
