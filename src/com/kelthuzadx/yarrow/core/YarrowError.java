package com.kelthuzadx.yarrow.core;

import jdk.vm.ci.common.JVMCIError;

public class YarrowError extends JVMCIError {
    public YarrowError(String msg) {
        super(msg);
    }
}
