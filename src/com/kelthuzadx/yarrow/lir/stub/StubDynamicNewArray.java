package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubDynamicNewArray extends RuntimeStub {
    private long addr;

    public StubDynamicNewArray() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_array");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::dynamic_new_array";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
