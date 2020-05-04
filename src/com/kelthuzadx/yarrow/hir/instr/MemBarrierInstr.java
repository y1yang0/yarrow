package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.code.MemoryBarriers;
import jdk.vm.ci.meta.JavaKind;

public class MemBarrierInstr extends HirInstr {
    private final int barrierType;

    public MemBarrierInstr(int barrierType) {
        super(JavaKind.Illegal);
        this.barrierType = barrierType;
    }

    public int getBarrierType() {
        return barrierType;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: membar {}", super.id, MemoryBarriers.barriersString(barrierType));
    }
}
