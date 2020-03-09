package com.kelthuzadx.yarrow.ir.hir;

import com.kelthuzadx.yarrow.util.BasicType;

public class LoadIndexInstr extends Instruction {
    private Instruction array;
    private Instruction index;
    private Instruction length;
    private BasicType elementType;

    public LoadIndexInstr(Instruction array, Instruction index, Instruction length, BasicType elementType){
        this.index = index;
        this.length = length;
        this.elementType = elementType;
    }

}
