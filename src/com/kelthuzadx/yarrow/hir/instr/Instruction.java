package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.optimize.Visitable;
import jdk.vm.ci.meta.JavaKind;

import java.util.Optional;

/**
 * Instruction represents single SSA form instruction.
 *
 * @author kelthuzadx
 */
public abstract class Instruction implements Visitable {
    protected int id;
    private Instruction next;
    private Value value;

    Instruction(Value value) {
        this.id = IdGenerator.next();
        this.value = value;
        this.next = null;
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

    /**
     * Each time HirBuilder appends new SSA instruction into basic block, Ideal would apply
     * applies many local optimizations on this newly created single instruction, it may or
     * may not transform newly created instruction. Many classic optimization technique
     * such as constant folding, dead code will be combined together, so I called it "Ideal".
     * <p>
     * Note that NEVER RETURN NULL, if it can not transform to a optimized version, return this
     * directly.
     *
     * @return new instruction or `this`
     */
    public Instruction ideal() {
        return this;
    }

    private static class IdGenerator {
        private static int id;

        static int next() {
            return id++;
        }
    }
}

