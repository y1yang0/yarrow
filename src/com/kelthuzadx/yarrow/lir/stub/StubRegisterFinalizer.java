package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubRegisterFinalizer extends RuntimeStub {
    private long addr;

    public StubRegisterFinalizer() {
        this.addr = YarrowRuntime.access.getAddress("SharedRuntime::register_finalizer");
    }

    @Override
    public String name() {
        return "SharedRuntime::register_finalizer";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
