package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubExceptionHandlerForReturnAddress extends RuntimeStub {
    private long addr;

    public StubExceptionHandlerForReturnAddress() {
        this.addr = YarrowRuntime.access.getAddress("SharedRuntime::exception_handler_for_return_address");
    }

    @Override
    public String name() {
        return "SharedRuntime::exception_handler_for_return_address";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
