package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubNewInstance extends RuntimeStub {
    private long addr;

    public StubNewInstance() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::new_instance");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::new_instance";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
