package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaMethod;
import jdk.vm.ci.meta.JavaType;

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
}
