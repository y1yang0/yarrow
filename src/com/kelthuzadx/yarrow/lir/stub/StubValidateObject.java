package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubValidateObject extends RuntimeStub {
    private long addr;

    public StubValidateObject() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::validate_object");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::validate_object";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
