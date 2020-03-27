package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubResolveStringBySymbol extends RuntimeStub {
    private long addr;

    public StubResolveStringBySymbol() {
        this.addr = YarrowRuntime.access.getAddress("CompilerRuntime::resolve_string_by_symbol");
    }

    @Override
    public String name() {
        return "CompilerRuntime::resolve_string_by_symbol";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
