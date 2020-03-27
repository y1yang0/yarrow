package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubResolveKlassBySymbol extends RuntimeStub {
    private long addr;

    public StubResolveKlassBySymbol() {
        this.addr = YarrowRuntime.access.getAddress("CompilerRuntime::resolve_klass_by_symbol");
    }

    @Override
    public String name() {
        return "CompilerRuntime::resolve_klass_by_symbol";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
