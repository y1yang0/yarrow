package com.kelthuzadx.yarrow.lir.operand;

import jdk.vm.ci.code.StackSlot;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.ValueKind;

public class StackVar extends LirOperand {
    private StackSlot stackSlot;

    StackVar(ValueKind<?> kind, int offset, boolean addFrameSize) {
        this.stackSlot = StackSlot.get(kind, offset, addFrameSize);
    }

    @Override
    public JavaKind getJavaKind() {
        return null;
    }

    @Override
    public boolean isConstValue() {
        return false;
    }

    @Override
    public boolean isVirtualRegister() {
        return false;
    }

    @Override
    public boolean isStackVar() {
        return true;
    }

    @Override
    public boolean isAddress() {
        return false;
    }
}
