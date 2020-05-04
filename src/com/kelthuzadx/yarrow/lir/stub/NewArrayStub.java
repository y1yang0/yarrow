package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.lir.operand.VirtualRegister;

public class NewArrayStub extends RuntimeStub {
    private final VirtualRegister length;
    private final VirtualRegister klass;
    private final VirtualRegister ret;

    public NewArrayStub(VirtualRegister length, VirtualRegister klass, VirtualRegister ret) {
        super(VmStub.StubNewArray);
        this.length = length;
        this.klass = klass;
        this.ret = ret;
    }

    @Override
    public String toString() {
        return stub.toString();
    }
}
