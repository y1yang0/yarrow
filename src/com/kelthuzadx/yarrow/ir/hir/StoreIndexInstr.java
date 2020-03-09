package com.kelthuzadx.yarrow.ir.hir;

import com.kelthuzadx.yarrow.ir.value.ValueType;

public class StoreIndexInstr extends Instruction {
    private Instruction array;
    private Instruction index;
    private Instruction length;
    private ValueType elementType;
    private Instruction value;

    public StoreIndexInstr(Instruction array, Instruction index, Instruction length, ValueType elementType, Instruction value) {
        this.array = array;
        this.index = index;
        this.length = length;
        this.elementType = elementType;
        this.value = value;
    }
}
