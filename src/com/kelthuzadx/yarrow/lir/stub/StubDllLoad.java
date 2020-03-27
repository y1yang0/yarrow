package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubDllLoad extends RuntimeStub {
    private long addr;

    public StubDllLoad() {
        this.addr = YarrowRuntime.access.getAddress("os::dll_load");
    }

    @Override
    public String name() {
        return "os::dll_load";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
