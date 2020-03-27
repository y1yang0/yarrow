package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubOsrMigrationEnd extends RuntimeStub {
    private long addr;

    public StubOsrMigrationEnd() {
        this.addr = YarrowRuntime.access.getAddress("SharedRuntime::OSR_migration_end");
    }

    @Override
    public String name() {
        return "SharedRuntime::OSR_migration_end";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
