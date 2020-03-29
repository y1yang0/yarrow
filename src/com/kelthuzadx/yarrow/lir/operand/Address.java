package com.kelthuzadx.yarrow.lir.operand;

import jdk.vm.ci.meta.JavaKind;

public class Address extends LirOperand {
    private LirOperand base;
    private LirOperand index;
    private int scale;
    private int displacement;
    private JavaKind type;

    // displacement[base+index*scale]
    Address(LirOperand base, LirOperand index, int scale, int displacement, JavaKind type) {
        this.base = base;
        this.index = index;
        this.scale = scale;
        this.displacement = displacement;
        this.type = type;
    }

    // displacement[base+index*1]
    Address(LirOperand base, LirOperand index, int displacement, JavaKind type) {
        this(base, index, 1, displacement, type);
    }

    // [base+index*1]
    Address(LirOperand base, LirOperand index, JavaKind type) {
        this(base, index, 1, 0, type);
    }

    // displacement[base]
    Address(LirOperand base, int displacement, JavaKind type) {
        this(base, LirOperand.illegal, 1, displacement, type);
    }

    // [base]
    Address(LirOperand base, JavaKind type) {
        this(base, LirOperand.illegal, 1, 0, type);
    }

    @Override
    public JavaKind getJavaKind() {
        return type;
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
    public boolean isAddress() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (displacement != 0) {
            result.append(displacement);
        }
        result.append("[").append(base);
        if (index != illegal) {
            result.append("+" + index);
            if (scale != 1) {
                result.append("*" + scale);
            }
        }
        result.append("]");

        return result.toString();
    }
}
