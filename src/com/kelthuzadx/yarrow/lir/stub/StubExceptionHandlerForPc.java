package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubExceptionHandlerForPc extends RuntimeStub {
    private long addr;

    public StubExceptionHandlerForPc() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::exception_handler_for_pc");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::exception_handler_for_pc";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
