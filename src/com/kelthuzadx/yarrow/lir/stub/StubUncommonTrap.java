package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubUncommonTrap extends RuntimeStub {
    private long addr;

    public StubUncommonTrap() {
        this.addr = YarrowRuntime.access.getAddress("Deoptimization::uncommon_trap");
    }

    @Override
    public String name() {
        return "Deoptimization::uncommon_trap";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
