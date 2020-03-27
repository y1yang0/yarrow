package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubDllLookup extends RuntimeStub {
    private long addr;

    public StubDllLookup() {
        this.addr = YarrowRuntime.access.getAddress("os::dll_lookup");
    }

    @Override
    public String name() {
        return "os::dll_lookup";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
