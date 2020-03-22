package com.kelthuzadx.yarrow.util;

import com.kelthuzadx.yarrow.core.YarrowError;

public class CompilerErrors {

    public static void bailOut() {
        throw new YarrowError("compilation bail out");
    }

    public static void bailOut(String msg) {
        throw new YarrowError(msg);
    }
}

