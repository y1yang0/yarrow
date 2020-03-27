package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubVmMessage extends RuntimeStub {
    private long addr;

    public StubVmMessage() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::vm_message");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::vm_message";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
