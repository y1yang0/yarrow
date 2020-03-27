package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubFetchUnrollInfo extends RuntimeStub {
    private long addr;

    public StubFetchUnrollInfo() {
        this.addr = YarrowRuntime.access.getAddress("Deoptimization::fetch_unroll_info");
    }

    @Override
    public String name() {
        return "Deoptimization::fetch_unroll_info";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
