package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;

public abstract class AccessArrayInstr extends HirInstruction {
    protected HirInstruction array;

    public AccessArrayInstr(Value value, HirInstruction array) {
        super(value);
        this.array = array;
    }

    public HirInstruction getArray() {
        return array;
    }
}
