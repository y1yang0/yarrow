package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubEnableStackReservedZone extends RuntimeStub {
    private long addr;

    public StubEnableStackReservedZone() {
        this.addr = YarrowRuntime.access.getAddress("SharedRuntime::enable_stack_reserved_zone");
    }

    @Override
    public String name() {
        return "SharedRuntime::enable_stack_reserved_zone";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
