package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubObjectNotify extends RuntimeStub {
    private long addr;

    public StubObjectNotify() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::object_notify");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::object_notify";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
