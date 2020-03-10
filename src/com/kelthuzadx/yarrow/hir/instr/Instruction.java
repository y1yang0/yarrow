package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.value.Value;
import com.kelthuzadx.yarrow.hir.value.ValueType;

public class Instruction {
    private int id;
    private Instruction next;
    private Value value;

    Instruction(){
        this.id = IdGenerator.next();
        this.value = new Value(ValueType.Illegal);
        this.next = null;
    }

    Instruction(Value value){
        this.id = IdGenerator.next();
        this.value = value;
        this.next = null;
    }

    public void setNext(Instruction next) {
        this.next = next;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public boolean isType(ValueType type){
        return this.value.isType(type);
    }

    private static class IdGenerator{
        private static int id;

        static int next(){
            return id++;
        }
    }
}

