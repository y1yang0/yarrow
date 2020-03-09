package com.kelthuzadx.yarrow.ir.hir;

import com.kelthuzadx.yarrow.ir.value.Value;
import com.kelthuzadx.yarrow.ir.value.ValueType;

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

