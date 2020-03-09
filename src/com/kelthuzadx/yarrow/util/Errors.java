package com.kelthuzadx.yarrow.util;

import com.kelthuzadx.yarrow.core.YarrowError;

public class Errors {
    public static void shouldNotReachHere(){
        throw new YarrowError("should not reach here");
    }
}
