package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class MonitorExitInstr extends StateInstr {
    private HirInstr lock;

    public MonitorExitInstr(HirInstr lock) {
        super(JavaKind.Illegal, null);
        this.lock = lock;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: unlock i{}", super.id, lock.id);
    }
}
