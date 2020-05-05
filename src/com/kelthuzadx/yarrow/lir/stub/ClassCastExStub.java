package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;

public class ClassCastExStub extends RuntimeStub {
    private final LirOperand result;
    private VmStub stub;

    public ClassCastExStub(LirOperand result) {
        super(VmStub.StubThrowClassCastException);
        this.result = result;
    }
}
