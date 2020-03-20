package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;

public abstract class AccessArrayInstr extends Instruction {
    protected Instruction array;

    public AccessArrayInstr(Value value, Instruction array) {
        super(value);
        this.array = array;
    }

    public Instruction getArray() {
        return array;
    }
}
