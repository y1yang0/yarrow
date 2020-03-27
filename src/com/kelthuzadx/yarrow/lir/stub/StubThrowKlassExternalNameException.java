package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubThrowKlassExternalNameException extends RuntimeStub {
    private long addr;

    public StubThrowKlassExternalNameException() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::throw_klass_external_name_exception");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::throw_klass_external_name_exception";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
