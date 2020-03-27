package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubDrem extends RuntimeStub {
    private long addr;

    public StubDrem() {
        this.addr = YarrowRuntime.access.getAddress("SharedRuntime::drem");
    }

    @Override
    public String name() {
        return "SharedRuntime::drem";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
