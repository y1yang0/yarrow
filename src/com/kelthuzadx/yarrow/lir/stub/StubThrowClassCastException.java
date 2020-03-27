package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubThrowClassCastException extends RuntimeStub {
    private long addr;

    public StubThrowClassCastException() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::throw_class_cast_exception");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::throw_class_cast_exception";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
