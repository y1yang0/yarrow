package com.kelthuzadx.yarrow.lir.operand;

import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;

public class ConstValue extends LirOperand {
    private JavaConstant constant;

    public ConstValue(JavaConstant constant) {
        this.constant = constant;
    }

    @Override
    public JavaKind getJavaKind() {
        return constant.getJavaKind();
    }

    @Override
    public String toString() {
        return constant.toValueString();
    }
}
