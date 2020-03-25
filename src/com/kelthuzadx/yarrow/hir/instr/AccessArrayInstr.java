package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

public abstract class AccessArrayInstr extends HirInstr {
    protected HirInstr array;

    public AccessArrayInstr(JavaKind type, HirInstr array) {
        super(type);
        this.array = array;
    }

    public HirInstr getArray() {
        return array;
    }
}
