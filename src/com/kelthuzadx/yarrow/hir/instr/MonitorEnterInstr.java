package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class MonitorEnterInstr extends StateInstr {
    private Instruction lock;

    public MonitorEnterInstr(Instruction lock, VmState stateBefore) {
        super(new Value(JavaKind.Illegal), stateBefore);
        this.lock = lock;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: lock i{}", super.id, lock.id);
    }
}
