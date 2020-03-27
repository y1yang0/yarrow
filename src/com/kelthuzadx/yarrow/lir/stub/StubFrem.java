package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubFrem extends RuntimeStub {
    private long addr;

    public StubFrem() {
        this.addr = YarrowRuntime.access.getAddress("SharedRuntime::frem");
    }

    @Override
    public String name() {
        return "SharedRuntime::frem";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
