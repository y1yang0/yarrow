package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;

public class Op2Instr extends Instruction {
    protected int opcode;
    protected Instruction left;
    protected Instruction right;

    public Op2Instr(Value value, int opcode, Instruction left, Instruction right) {
        super(value);
        this.opcode = opcode;
        this.left = left;
        this.right = right;
    }
}
