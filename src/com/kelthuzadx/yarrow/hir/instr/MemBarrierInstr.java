package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.CompilerErrors;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.code.MemoryBarriers;
import jdk.vm.ci.meta.JavaKind;

public class MemBarrierInstr extends HirInstruction {
    private int barrierType;

    public MemBarrierInstr(int barrierType) {
        super(new Value(JavaKind.Illegal));
        this.barrierType = barrierType;
    }

    @Override
    public String toString() {
        String type = "";
        switch (barrierType) {
            case MemoryBarriers.LOAD_LOAD:
                type = "LoadLoad";
                break;
            case MemoryBarriers.LOAD_STORE:
                type = "LoadStore";
                break;
            case MemoryBarriers.STORE_LOAD:
                type = "StoreLoad";
                break;
            case MemoryBarriers.STORE_STORE:
                type = "StoreStore";
                break;
            default:
                CompilerErrors.bailOut();
        }
        return Logger.format("i{}: membar {}", super.id, type);
    }
}
