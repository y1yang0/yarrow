package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubNewMultiArray extends RuntimeStub {
    private long addr;

    public StubNewMultiArray() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::new_multi_array");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::new_multi_array";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
