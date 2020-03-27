package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubNewArray extends RuntimeStub {
    private long addr;

    public StubNewArray() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::new_array");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::new_array";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
