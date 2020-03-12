package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;

public class NegateInstr extends Instruction {
    private Instruction left;

    public NegateInstr(Instruction left) {
        super(new Value(left.getType()));
        this.left = left;
    }

    @Override
    public String toString() {
        return Logger.f("i{}: -i{}]", super.id, left.id);
    }
}
