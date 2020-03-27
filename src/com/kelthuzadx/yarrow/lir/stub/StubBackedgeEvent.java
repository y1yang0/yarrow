package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubBackedgeEvent extends RuntimeStub {
    private long addr;

    public StubBackedgeEvent() {
        this.addr = YarrowRuntime.access.getAddress("CompilerRuntime::backedge_event");
    }

    @Override
    public String name() {
        return "CompilerRuntime::backedge_event";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
