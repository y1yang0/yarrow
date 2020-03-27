package com.kelthuzadx.yarrow.util;

import com.kelthuzadx.yarrow.core.YarrowError;
import jdk.vm.ci.code.BailoutException;

public class CompilerErrors {

    public static void bailOut() {
        throw new BailoutException("compilation bail out");
    }

    public static void bailOut(String msg) {
        throw new BailoutException(msg);
    }
}

