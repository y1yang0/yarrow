package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubJavatimenanos extends RuntimeStub {
    private long addr;

    public StubJavatimenanos() {
        this.addr = YarrowRuntime.access.getAddress("os::javaTimeNanos");
    }

    @Override
    public String name() {
        return "os::javaTimeNanos";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
