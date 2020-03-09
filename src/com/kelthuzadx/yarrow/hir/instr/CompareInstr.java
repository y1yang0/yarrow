package com.kelthuzadx.yarrow.hir.instr;

public class CompareInstr extends Op2Instr {
    public CompareInstr(int opcode, Instruction left, Instruction right) {
        super(opcode, left, right);
    }
}
