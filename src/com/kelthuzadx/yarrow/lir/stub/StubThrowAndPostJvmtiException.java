package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubThrowAndPostJvmtiException extends RuntimeStub {
    private long addr;

    public StubThrowAndPostJvmtiException() {
        this.addr = YarrowRuntime.access.getAddress("JVMCIRuntime::throw_and_post_jvmti_exception");
    }

    @Override
    public String name() {
        return "JVMCIRuntime::throw_and_post_jvmti_exception";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
