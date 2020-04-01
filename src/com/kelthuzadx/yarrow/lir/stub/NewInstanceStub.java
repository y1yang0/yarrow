package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.lir.operand.VirtualRegister;
import jdk.vm.ci.hotspot.HotSpotResolvedObjectType;

public class NewInstanceStub extends RuntimeStub {
    private HotSpotResolvedObjectType klassType;
    private VirtualRegister klass;
    private VirtualRegister ret;

    public NewInstanceStub(HotSpotResolvedObjectType klassType, VirtualRegister klass, VirtualRegister ret) {
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
