package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubInvocationEvent extends RuntimeStub {
    private long addr;

    public StubInvocationEvent() {
        this.addr = YarrowRuntime.access.getAddress("CompilerRuntime::invocation_event");
    }

    @Override
    public String name() {
        return "CompilerRuntime::invocation_event";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
