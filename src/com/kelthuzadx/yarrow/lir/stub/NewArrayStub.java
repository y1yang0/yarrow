package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.lir.operand.XRegister;

public class NewArrayStub extends RuntimeStub {
    private final XRegister length;
    private final XRegister klass;
    private final XRegister ret;

    public NewArrayStub(XRegister length, XRegister klass, XRegister ret) {
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
