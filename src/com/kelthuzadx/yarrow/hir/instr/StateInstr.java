package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;

public abstract class StateInstr extends Instruction {
    private VmState state;

    StateInstr(Value value, VmState stateBefore) {
        super(value);
        this.state = stateBefore;
    }


    public VmState getVmState() {
        return state;
    }

    public void setVmState(VmState state) {
        this.state = state;
    }
}
