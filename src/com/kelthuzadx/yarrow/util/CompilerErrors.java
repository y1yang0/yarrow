package com.kelthuzadx.yarrow.util;

import jdk.vm.ci.code.BailoutException;

public class CompilerErrors {

    public static void bailOut() {
        throw new BailoutException("compilation bail out");
    }

    public static void bailOut(String msg) {
        throw new BailoutException(msg);
    }
}

