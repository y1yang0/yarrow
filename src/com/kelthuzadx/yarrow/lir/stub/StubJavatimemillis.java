package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubJavatimemillis extends RuntimeStub {
    private long addr;

    public StubJavatimemillis() {
        this.addr = YarrowRuntime.access.getAddress("os::javaTimeMillis");
    }

    @Override
    public String name() {
        return "os::javaTimeMillis";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
