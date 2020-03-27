package com.kelthuzadx.yarrow.lir.operand;


import jdk.vm.ci.meta.JavaKind;

public abstract class LirOperand {
    public static final LirOperand illegal = new LirOperand() {
        @Override
        public JavaKind getJavaKind() {
            return JavaKind.Illegal;
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
            return false;
        }

        @Override
        public String toString() {
            return "-";
        }
    };

    public abstract JavaKind getJavaKind();

    public abstract boolean isConstValue();

    public abstract boolean isVirtualRegister();

    public abstract boolean isStackVar();
}
