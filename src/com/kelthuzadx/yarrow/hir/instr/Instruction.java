package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.Visitable;
import jdk.vm.ci.meta.JavaKind;

import java.util.Optional;

public class Instruction implements Visitable {
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

    public int id() {
        return id;
    }

    public <T> T value() {
        Optional<T> val = value.getValue();
        return val.orElse(null);
    }

    public boolean isType(JavaKind type) {
        return value.getType() == type;
    }

    public JavaKind type() {
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
}

