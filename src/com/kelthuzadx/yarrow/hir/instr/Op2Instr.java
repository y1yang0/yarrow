package com.kelthuzadx.yarrow.hir.instr;

public class Op2Instr extends Instruction {
    private int opcode;
    private Instruction left;
    private Instruction right;

    public Op2Instr(int opcode, Instruction left, Instruction right) {
        this.opcode = opcode;
        this.left = left;
        this.right = right;
    }
}
