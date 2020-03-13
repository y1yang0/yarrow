package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class ArrayLenInstr extends AccessArrayInstr {

    public ArrayLenInstr(Instruction array) {
        super(new Value(JavaKind.Int), array);
    }

    @Override
    public String toString() {
        return Logger.format("i{}: arraylen i{}", super.id, super.array.id);
    }
}
