package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class MonitorExitInstr extends StateInstr {
    private Instruction lock;

    public MonitorExitInstr(Instruction lock) {
        super(new Value(JavaKind.Illegal), null);
        this.lock = lock;
    }

    @Override
    public String toString() {
        return Logger.f("i{}: monitoreixt i{}", super.id, lock.id);
    }
}
