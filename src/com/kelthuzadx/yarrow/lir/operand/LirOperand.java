package com.kelthuzadx.yarrow.lir.operand;


import jdk.vm.ci.meta.JavaKind;

public abstract class LirOperand {
    public static final LirOperand illegal = new LirOperand() {
        @Override
        public JavaKind getJavaKind() {
            return JavaKind.Illegal;
        }
    };

    public abstract JavaKind getJavaKind();
}
