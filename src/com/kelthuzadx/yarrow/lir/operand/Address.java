package com.kelthuzadx.yarrow.lir.operand;

import com.kelthuzadx.yarrow.core.YarrowError;
import jdk.vm.ci.meta.JavaKind;

public class Address extends LirOperand {
    private final LirOperand base;
    private final LirOperand index;
    private final int scale;
    private final int displacement;
    private final JavaKind type;

    // displacement[base+index*scale]
    public Address(LirOperand base, LirOperand index, int scale, int displacement, JavaKind type) {
        this.base = base;
        this.index = index;
        this.scale = scale;
        this.displacement = displacement;
        this.type = type;
    }

    // displacement[base+index*1]
    public Address(LirOperand base, LirOperand index, int displacement, JavaKind type) {
        this(base, index, 1, displacement, type);
    }

    // [base+index*1]
    public Address(LirOperand base, LirOperand index, JavaKind type) {
        this(base, index, 1, 0, type);
    }

    // displacement[base]
    public Address(LirOperand base, int displacement, JavaKind type) {
        this(base, LirOperand.illegal, 1, displacement, type);
    }

    // [base]
    public Address(LirOperand base, JavaKind type) {
        this(base, LirOperand.illegal, 1, 0, type);
    }

    public static int scaleFor(JavaKind type) {
        switch (type) {
            case Boolean:
            case Byte:
                return 1;
            case Char:
            case Short:
                return 2;
            case Float:
            case Int:
                return 4;
            case Double:
            case Long:
            case Object:
                return 8;
            default:
                YarrowError.shouldNotReachHere();
        }
        return Integer.MAX_VALUE;
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
