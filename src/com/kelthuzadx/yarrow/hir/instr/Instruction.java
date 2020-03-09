package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.value.Value;
import com.kelthuzadx.yarrow.hir.value.ValueType;

public class Instruction {
    private int id;
    private Instruction next;
    private Value value;

    Instruction(){}

    public void setNext(Instruction next) {
        this.next = next;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public boolean isType(ValueType type){
        return this.value.isType(type);
    }
}

