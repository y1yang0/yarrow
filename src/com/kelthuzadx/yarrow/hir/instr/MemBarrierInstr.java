package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

public class MemBarrierInstr extends Instruction {
    private int barrierType;

    public MemBarrierInstr(int barrierType) {
        super(new Value(JavaKind.Illegal));
        this.barrierType = barrierType;
    }
}
