package com.kelthuzadx.yarrow.lir.operand;

import com.kelthuzadx.yarrow.core.YarrowRuntime;
import com.kelthuzadx.yarrow.util.Increment;
import jdk.vm.ci.code.Register;
import jdk.vm.ci.meta.AllocatableValue;
import jdk.vm.ci.meta.JavaKind;

public class XRegister extends AllocatableValue {
    private static final int VREGID_BASE = YarrowRuntime.arch.getRegisters().size() + 1;
    private static final Register.RegisterCategory virtual = new Register.RegisterCategory("Virtual");
    private final Register register;
    private final boolean isVirtual;
    private JavaKind type;

    public XRegister(JavaKind type) {
        super(new LirValueKindFactory().getValueKind(type));
        this.type = type;
        int regId = VREGID_BASE + Increment.next(XRegister.class);
        this.register = new Register(regId, regId, "vreg" + regId, virtual);
        this.isVirtual = true;
    }

    public XRegister(Register register) {
        super(null);
        this.register = register;
        this.isVirtual = false;
    }

    public boolean isVirtualRegister() {
        return isVirtual;
    }

    public int getVirtualRegisterId() {
        return register.number;
    }


    @Override
    public String toString() {
        if (register.getRegisterCategory().equals(virtual)) {
            return "v" + register.number;
        }
        return register.toString();
    }
}
