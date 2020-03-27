package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubDynamicNewInstance extends RuntimeStub {
    private long addr;

    public StubDynamicNewInstance() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_instance");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::dynamic_new_instance";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
