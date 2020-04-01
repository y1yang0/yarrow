package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;

public class ClassCastExStub extends RuntimeStub {
    private VmStub stub;
    private LirOperand result;

    public ClassCastExStub(LirOperand result) {
        super(VmStub.StubThrowClassCastException);
        this.result = result;
    }
}
