package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;

public class NegateInstr extends Instruction {
    private Instruction left;

    public NegateInstr(Instruction left) {
        super(new Value(left.getType()));
        this.left = left;
    }
}
