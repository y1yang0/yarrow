package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubLoadAndClearException extends RuntimeStub {
    private long addr;

    public StubLoadAndClearException() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::load_and_clear_exception");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::load_and_clear_exception";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
