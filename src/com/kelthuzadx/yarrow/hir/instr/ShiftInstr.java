package com.kelthuzadx.yarrow.hir.instr;

public class ShiftInstr extends Op2Instr{
    public ShiftInstr(int opcode, Instruction left, Instruction right) {
        super(opcode, left, right);
    }
}
