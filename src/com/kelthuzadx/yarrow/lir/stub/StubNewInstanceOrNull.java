package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubNewInstanceOrNull extends RuntimeStub {
    private long addr;

    public StubNewInstanceOrNull() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::new_instance_or_null");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::new_instance_or_null";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
