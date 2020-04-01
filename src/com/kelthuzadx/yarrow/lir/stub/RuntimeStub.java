package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.lir.instr.LabelInstr;
import com.kelthuzadx.yarrow.lir.operand.VirtualRegister;
import jdk.vm.ci.hotspot.HotSpotResolvedObjectType;

@SuppressWarnings("unused")
public class RuntimeStub {
    protected VmStub stub;
    private LabelInstr trampoline;
    private LabelInstr continuation;

    public RuntimeStub(VmStub stub) {
        this.stub = stub;
        this.trampoline = new LabelInstr();
        this.continuation = new LabelInstr();
    }


    public LabelInstr getTrampoline() {
        return trampoline;
    }

    public LabelInstr getContinuation() {
        return continuation;
    }
}
