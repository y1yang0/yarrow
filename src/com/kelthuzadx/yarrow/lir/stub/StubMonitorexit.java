package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubMonitorexit extends RuntimeStub {
    private long addr;

    public StubMonitorexit() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::monitorexit");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::monitorexit";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
