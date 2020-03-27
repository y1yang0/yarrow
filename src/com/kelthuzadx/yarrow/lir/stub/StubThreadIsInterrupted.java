package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubThreadIsInterrupted extends RuntimeStub {
    private long addr;

    public StubThreadIsInterrupted() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::thread_is_interrupted");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::thread_is_interrupted";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
