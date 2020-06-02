package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.lir.operand.XRegister;
import jdk.vm.ci.hotspot.HotSpotResolvedObjectType;

public class NewInstanceStub extends RuntimeStub {
    private final HotSpotResolvedObjectType klassType;
    private final XRegister klass;
    private final XRegister ret;

    public NewInstanceStub(HotSpotResolvedObjectType klassType, XRegister klass, XRegister ret) {
        super(VmStub.StubNewInstance);
        this.klassType = klassType;
        this.klass = klass;
        this.ret = ret;
    }

    @Override
    public String toString() {
        return stub.toString();
    }
}
