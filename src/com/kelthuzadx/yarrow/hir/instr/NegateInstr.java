package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.value.Value;

public class NegateInstr extends Instruction {
    private Instruction left;

    public NegateInstr(Instruction left) {
        this.left = left;
    }
}
