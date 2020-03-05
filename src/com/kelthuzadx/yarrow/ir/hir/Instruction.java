package com.kelthuzadx.yarrow.ir.hir;

import com.kelthuzadx.yarrow.ir.value.Value;

public class Instruction {
    private int id;
    private Instruction next;
    private Value type;

    Instruction(){}

    public void setNext(Instruction next) {
        this.next = next;
    }

    public void setType(Value type) {
        this.type = type;
    }
}

