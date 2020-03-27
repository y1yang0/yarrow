package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubTestDeoptimizeCallInt extends RuntimeStub {
    private long addr;

    public StubTestDeoptimizeCallInt() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::test_deoptimize_call_int");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::test_deoptimize_call_int";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
