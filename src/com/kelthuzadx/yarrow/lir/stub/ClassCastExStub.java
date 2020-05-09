package com.kelthuzadx.yarrow.lir.stub;

import jdk.vm.ci.meta.AllocatableValue;

public class ClassCastExStub extends RuntimeStub {
    private final AllocatableValue result;
    private VmStub stub;

    public ClassCastExStub(AllocatableValue result) {
        super(VmStub.StubThrowClassCastException);
        this.result = result;
    }
}
