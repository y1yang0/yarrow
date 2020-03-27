package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubResolveDynamicInvoke extends RuntimeStub {
    private long addr;

    public StubResolveDynamicInvoke() {
        this.addr = YarrowRuntime.access.getAddress("CompilerRuntime::resolve_dynamic_invoke");
    }

    @Override
    public String name() {
        return "CompilerRuntime::resolve_dynamic_invoke";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
