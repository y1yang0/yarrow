package com.kelthuzadx.yarrow.util;

import com.kelthuzadx.yarrow.core.YarrowError;

public class CompilerErrors {
    public static void shouldNotReachHere(){
        throw new YarrowError("should not reach here");
    }

    public static void unsupported(){
        throw new YarrowError("unsupported feature");
    }
}
