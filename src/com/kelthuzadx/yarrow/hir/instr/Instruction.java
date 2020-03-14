package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

import java.util.Optional;

public class Instruction {
    protected int id;
    private Instruction next;
    private Value value;

    Instruction(Value value) {
        this.id = IdGenerator.next();
        this.value = value;
        this.next = null;
    }

    public static void assertType(Instruction value, JavaKind rhs) {
        if (!value.isType(rhs)) {
            throw new YarrowError("Type Mismatch");
        }
    }

    public int getId() {
        return id;
    }

    public Optional<Object> getValue() {
        return value.getValue();
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public boolean isType(JavaKind type) {
        return this.value.isType(type);
    }

    public JavaKind getType() {
        return value.getType();
    }

    public Instruction getNext() {
        return next;
    }

    public void setNext(Instruction next) {
        this.next = next;
    }

    private static class IdGenerator {
        private static int id;

        static int next() {
            return id++;
        }
    }

    public static boolean matchType(Instruction ia, Instruction ib){
        if(ia.value.getType()!=ib.value.getType()){
            return false;
        }
        return true;
    }
}

