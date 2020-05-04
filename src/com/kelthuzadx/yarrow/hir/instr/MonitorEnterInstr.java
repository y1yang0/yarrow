package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class MonitorEnterInstr extends StateInstr {
    private final HirInstr lock;

    public MonitorEnterInstr(HirInstr lock, VmState stateBefore) {
        super(JavaKind.Illegal, stateBefore);
        this.lock = lock;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: lock i{}", super.id, lock.id);
    }
}
