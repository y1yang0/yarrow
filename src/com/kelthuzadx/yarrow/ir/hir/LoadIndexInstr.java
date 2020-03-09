package com.kelthuzadx.yarrow.ir.hir;

import com.kelthuzadx.yarrow.ir.value.Value;
import com.kelthuzadx.yarrow.ir.value.ValueType;

public class LoadIndexInstr extends Instruction {
    private Instruction array;
    private Instruction index;
    private Instruction length;
    private ValueType elementType;

    public LoadIndexInstr(Instruction array, Instruction index, Instruction length, ValueType elementType){
        this.index = index;
        this.length = length;
        this.elementType = elementType;
    }

}
