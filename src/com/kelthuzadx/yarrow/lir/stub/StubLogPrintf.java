package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubLogPrintf extends RuntimeStub {
    private long addr;

    public StubLogPrintf() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::log_printf");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::log_printf";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
