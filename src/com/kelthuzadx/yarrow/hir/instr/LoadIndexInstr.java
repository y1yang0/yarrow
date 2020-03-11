package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.ValueType;

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
