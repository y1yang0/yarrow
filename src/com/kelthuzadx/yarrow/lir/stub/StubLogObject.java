package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubLogObject extends RuntimeStub {
    private long addr;

    public StubLogObject() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::log_object");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::log_object";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
