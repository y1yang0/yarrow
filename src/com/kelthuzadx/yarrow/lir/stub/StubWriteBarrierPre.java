package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubWriteBarrierPre extends RuntimeStub {
    private long addr;

    public StubWriteBarrierPre() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::write_barrier_pre");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::write_barrier_pre";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
