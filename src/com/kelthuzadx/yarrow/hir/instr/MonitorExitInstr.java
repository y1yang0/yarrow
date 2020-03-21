package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class MonitorExitInstr extends StateInstr {
    private HirInstruction lock;

    public MonitorExitInstr(HirInstruction lock) {
        super(new Value(JavaKind.Illegal), null);
        this.lock = lock;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: unlock i{}", super.id, lock.id);
    }
}
