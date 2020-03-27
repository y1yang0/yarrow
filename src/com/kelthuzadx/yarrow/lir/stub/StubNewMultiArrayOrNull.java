package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubNewMultiArrayOrNull extends RuntimeStub {
    private long addr;

    public StubNewMultiArrayOrNull() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::new_multi_array_or_null");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::new_multi_array_or_null";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
