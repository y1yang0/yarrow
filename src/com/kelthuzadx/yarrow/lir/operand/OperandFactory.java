package com.kelthuzadx.yarrow.lir.operand;

import jdk.vm.ci.code.Register;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;

public class OperandFactory {
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
