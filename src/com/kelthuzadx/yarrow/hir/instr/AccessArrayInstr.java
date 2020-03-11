package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;

public class AccessArrayInstr extends Instruction {
    private Instruction array;

    public AccessArrayInstr(Value value, Instruction array) {
        super(value);
        this.array = array;
    }
}
