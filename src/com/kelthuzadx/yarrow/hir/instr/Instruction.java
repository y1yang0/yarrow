package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

public class Instruction {
    private int id;
    private Instruction next;
    private Value value;

    Instruction(Value value){
        this.id = IdGenerator.next();
        this.value = value;
        this.next = null;
    }

    public static void assertType(Instruction value, JavaKind rhs){
        if(!value.isType(rhs)){
            throw new YarrowError("Type Mismatch");
        }
    }

    public void setNext(Instruction next) {
        this.next = next;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public boolean isType(JavaKind type){
        return this.value.isType(type);
    }

    public JavaKind getType(){
        return value.getType();
    }

    private static class IdGenerator{
        private static int id;

        static int next(){
            return id++;
        }
    }
}

