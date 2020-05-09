package com.kelthuzadx.yarrow.lir.operand;

import jdk.vm.ci.meta.AllocatableValue;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;

public class ConstValue extends AllocatableValue {
    private final JavaConstant constant;

    public ConstValue(JavaConstant constant) {
        super(new LirValueKindFactory().getValueKind(constant.getJavaKind()));
        this.constant = constant;
    }


    @Override
    public String toString() {
        if (constant.getJavaKind() == JavaKind.Int) {
            return "0x" + Integer.toHexString(Integer.parseInt(constant.toValueString()));
        } else if (constant.getJavaKind() == JavaKind.Long) {
            return "0x" + Long.toHexString(Long.parseLong(constant.toValueString()));
        }
        return constant.toValueString();
    }
}
