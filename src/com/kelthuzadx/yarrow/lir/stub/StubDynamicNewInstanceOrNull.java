package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubDynamicNewInstanceOrNull extends RuntimeStub {
    private long addr;

    public StubDynamicNewInstanceOrNull() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_instance_or_null");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::dynamic_new_instance_or_null";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
