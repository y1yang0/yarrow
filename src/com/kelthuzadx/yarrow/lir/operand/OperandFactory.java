package com.kelthuzadx.yarrow.lir.operand;

import jdk.vm.ci.code.Register;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;

public class OperandFactory {
    public static LirOperand createConstValue(JavaConstant constant) {
        return new ConstValue(constant);
    }

    public static LirOperand createVirtualRegister(JavaKind type) {
        return new VirtualRegister(type);
    }

    public static LirOperand createVirtualRegister(Register register) {
        return new VirtualRegister(register);
    }
}
