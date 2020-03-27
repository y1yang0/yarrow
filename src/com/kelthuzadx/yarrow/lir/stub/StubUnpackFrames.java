package com.kelthuzadx.yarrow.lir.stub;

import com.kelthuzadx.yarrow.core.YarrowRuntime;

public class StubUnpackFrames extends RuntimeStub {
    private long addr;

    public StubUnpackFrames() {
        this.addr = YarrowRuntime.access.getAddress("Deoptimization::unpack_frames");
    }

    @Override
    public String name() {
        return "Deoptimization::unpack_frames";
    }

    @Override
    public long getAddress() {
        return addr;
    }
}
