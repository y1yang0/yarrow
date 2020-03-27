package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubMonitorenter extends RuntimeStub {
    private long addr;

    public StubMonitorenter() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::monitorenter");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::monitorenter";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
