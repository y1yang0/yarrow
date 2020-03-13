package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;

public class PhiInstr extends Instruction {
    private int index; // negate number for stack, and positive number for local
    private BlockStartInstr block;

    public PhiInstr(Value value, int index, BlockStartInstr block) {
        super(value);
        this.index = index;
        this.block = block;
    }


    @Override
    public String toString() {
        return Logger.format("i{}: phi [{}]", super.id, index);
    }
}
