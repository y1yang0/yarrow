package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.lir.operand.VirtualRegister;

public class NewArrayStub extends RuntimeStub {
    private VirtualRegister length;
    private VirtualRegister klass;
    private VirtualRegister ret;

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
