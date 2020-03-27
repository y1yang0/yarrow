package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubNewArrayOrNull extends RuntimeStub {
    private long addr;

    public StubNewArrayOrNull() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::new_array_or_null");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::new_array_or_null";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
