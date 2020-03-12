package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaMethod;

public class ParamInstr extends Instruction {
    private boolean isReceiver;
    private int index;
    private JavaMethod method;

    public ParamInstr(Value value, JavaMethod method, boolean isReceiver, int index) {
        super(value);
        this.method = method;
        this.isReceiver = isReceiver;
        this.index = index;
    }

    @Override
    public String toString() {
        return Logger.f("i{}: param [{}]#{}", super.id, index, method.getName());
    }
}
