package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubLogPrimitive extends RuntimeStub {
    private long addr;

    public StubLogPrimitive() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::log_primitive");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::log_primitive";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
