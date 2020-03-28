package com.kelthuzadx.yarrow.lir.operand;

import jdk.vm.ci.code.Register;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;

public class OperandFactory {
    public static Address createAddress(LirOperand base, LirOperand index, int scale, int displacement, JavaKind type) {
        if (base == LirOperand.illegal) {
            throw new IllegalArgumentException("Base address should be valid");
        }
        if (scale != 1 && scale != 2 && scale != 4 && scale != 8) {
            throw new IllegalArgumentException("Scale must be one of 1,2,4,8");
        }
        return new Address(base, index, scale, displacement, type);
    }

    public static Address createAddress(LirOperand base, LirOperand index, int displacement, JavaKind type) {
        if (base == LirOperand.illegal) {
            throw new IllegalArgumentException("Base address should be valid");
        }
        return new Address(base, index, 1, displacement, type);
    }

    public static Address createAddress(LirOperand base, LirOperand index, JavaKind type) {
        if (base == LirOperand.illegal) {
            throw new IllegalArgumentException("Base address should be valid");
        }
        return new Address(base, index, 1, 0, type);
    }

    public static Address createAddress(LirOperand base, int displacement, JavaKind type) {
        if (base == LirOperand.illegal) {
            throw new IllegalArgumentException("Base address should be valid");
        }
        return new Address(base, LirOperand.illegal, 1, displacement, type);
    }

    public static Address createAddress(LirOperand base, JavaKind type) {
        if (base == LirOperand.illegal) {
            throw new IllegalArgumentException("Base address should be valid");
        }
        return new Address(base, LirOperand.illegal, 1, 0, type);
    }

    public static ConstValue createConstValue(JavaConstant constant) {
        return new ConstValue(constant);
    }

    public static VirtualRegister createVirtualRegister(JavaKind type) {
        return new VirtualRegister(type);
    }

    public static VirtualRegister createVirtualRegister(Register register) {
        return new VirtualRegister(register);
    }
}
