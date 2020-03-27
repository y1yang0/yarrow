package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubInitializeKlassBySymbol extends RuntimeStub {
    private long addr;

    public StubInitializeKlassBySymbol() {
        this.addr = YarrowRuntime.access.getAddress("CompilerRuntime::initialize_klass_by_symbol");
    }

    @Override
    public String name() {
        return "CompilerRuntime::initialize_klass_by_symbol";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
