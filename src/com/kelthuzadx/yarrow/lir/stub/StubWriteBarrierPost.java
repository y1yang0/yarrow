package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubWriteBarrierPost extends RuntimeStub {
    private long addr;

    public StubWriteBarrierPost() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::write_barrier_post");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::write_barrier_post";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
