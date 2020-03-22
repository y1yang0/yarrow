package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;

public abstract class AccessArrayInstr extends HirInstr {
    protected HirInstr array;

    public AccessArrayInstr(Value value, HirInstr array) {
        super(value);
        this.array = array;
    }

    public HirInstr getArray() {
        return array;
    }
}
