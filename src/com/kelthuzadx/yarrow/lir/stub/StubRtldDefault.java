package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubRtldDefault extends RuntimeStub {
    private long addr;

    public StubRtldDefault() {
        this.addr = YarrowRuntime.access.getAddress("RTLD_DEFAULT");
    }

    @Override
    String name() {
        return "RTLD_DEFAULT";
    }

    @Override
    long getAddress() {
        return addr;
    }
}
