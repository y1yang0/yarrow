package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

import java.util.Optional;

public class Instruction {
    protected int id;
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

    public Optional<Object> getValue() {
        return value.getValue();
    }

    public boolean isType(JavaKind type){
        return this.value.isType(type);
    }

    public JavaKind getType(){
        return value.getType();
    }

    public Instruction getNext(){
        return next;
    }

    private static class IdGenerator{
        private static int id;

        static int next(){
            return id++;
        }
    }
}

