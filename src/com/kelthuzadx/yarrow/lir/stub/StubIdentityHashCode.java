package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubIdentityHashCode extends RuntimeStub {
    private long addr;

    public StubIdentityHashCode() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::identity_hash_code");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::identity_hash_code";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
