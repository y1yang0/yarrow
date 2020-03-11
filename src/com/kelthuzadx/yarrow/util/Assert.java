package com.kelthuzadx.yarrow.util;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.Instruction;
import jdk.vm.ci.meta.JavaKind;

public class Assert {
    public static void matchType(Instruction value, JavaKind rhs){
        if(!value.isType(rhs)){
            throw new YarrowError("Type Mismatch");
        }
    }

    public static void matchInt(int a, int b){
        if(a!=b){
            throw new YarrowError("assertion failure");
        }
    }
}
