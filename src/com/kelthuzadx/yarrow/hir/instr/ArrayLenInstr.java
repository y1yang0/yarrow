package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

public class ArrayLenInstr extends AccessArrayInstr {

    public ArrayLenInstr(Instruction array){
        super(new Value(JavaKind.Int),array);
    }
}
