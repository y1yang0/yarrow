package com.kelthuzadx.yarrow.lir.operand;

import com.kelthuzadx.yarrow.core.YarrowRuntime;
import com.kelthuzadx.yarrow.util.Increment;
import jdk.vm.ci.code.Register;
import jdk.vm.ci.meta.JavaKind;

public class VirtualRegister extends LirOperand {
    private static final int VREGID_BASE = YarrowRuntime.arch.getRegisters().size() + 1;
    private static Register.RegisterCategory virtual = new Register.RegisterCategory("Virtual");
    private Register register;
    private JavaKind type;

    VirtualRegister(JavaKind type) {
        this.type = type;
        final int regId = VREGID_BASE + Increment.next(VirtualRegister.class);
        this.register = new Register(regId, regId, "vreg" + regId, virtual);
    }

    VirtualRegister(Register register) {
        this.register = register;
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
        return true;
    }

    @Override
    public boolean isStackVar() {
        return false;
    }

    @Override
    public boolean isAddress() {
        return false;
    }

    @Override
    public String toString() {
        if (register.getRegisterCategory().equals(virtual)
        ) {
            return "V" + register.number;
        }
        return register.toString();
    }
}
