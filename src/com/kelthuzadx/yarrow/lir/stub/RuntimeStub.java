package com.kelthuzadx.yarrow.lir.stub;

public abstract class RuntimeStub {
    static {
    }

    abstract String name();

    abstract long getAddress();

    @Override
    public String toString() {
        return name();
    }
}
