package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubResolveMethodBySymbolAndLoadCounters extends RuntimeStub {
    private long addr;

    public StubResolveMethodBySymbolAndLoadCounters() {
        this.addr = YarrowRuntime.access.getAddress("CompilerRuntime::resolve_method_by_symbol_and_load_counters");
    }

    @Override
    public String name() {
        return "CompilerRuntime::resolve_method_by_symbol_and_load_counters";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
