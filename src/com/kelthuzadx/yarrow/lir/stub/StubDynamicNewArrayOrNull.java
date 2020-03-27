package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubDynamicNewArrayOrNull extends RuntimeStub {
    private long addr;

    public StubDynamicNewArrayOrNull() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::dynamic_new_array_or_null");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::dynamic_new_array_or_null";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
