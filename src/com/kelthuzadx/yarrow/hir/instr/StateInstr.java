package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import jdk.vm.ci.meta.JavaKind;

public abstract class StateInstr extends HirInstr {
    private VmState state;

    StateInstr(JavaKind type, VmState stateBefore) {
        super(type);
        this.state = stateBefore;
    }


    public VmState getVmState() {
        return state;
    }

    public void setVmState(VmState state) {
        this.state = state;
    }
}
