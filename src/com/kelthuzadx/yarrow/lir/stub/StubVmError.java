package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubVmError extends RuntimeStub {
    private long addr;

    public StubVmError() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::vm_error");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::vm_error";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
