package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubObjectNotifyall extends RuntimeStub {
    private long addr;

    public StubObjectNotifyall() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::object_notifyAll");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::object_notifyAll";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
