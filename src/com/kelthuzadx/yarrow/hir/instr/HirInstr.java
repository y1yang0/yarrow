package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.lir.LirGenerator;
import com.kelthuzadx.yarrow.lir.operand.ConstValue;
import com.kelthuzadx.yarrow.lir.operand.VirtualRegister;
import com.kelthuzadx.yarrow.optimize.HirInstrVisitor;
import com.kelthuzadx.yarrow.optimize.Visitable;
import com.kelthuzadx.yarrow.util.Increment;
import jdk.vm.ci.meta.AllocatableValue;
import jdk.vm.ci.meta.JavaKind;

/**
 * Instruction represents single SSA form instruction.
 *
 * @author kelthuzadx
 */
public abstract class HirInstr implements Visitable {
    // High level IR
    protected int id;
    protected HirInstr next;
    protected JavaKind type;

    // Low level IR
    protected AllocatableValue operand;

    HirInstr(JavaKind type) {
        this.id = Increment.next(HirInstr.class);
        this.type = type;
        this.next = null;
    }

    public int id() {
        return id;
    }

    public boolean isType(JavaKind type) {
        return this.type == type;
    }

    public JavaKind type() {
        return this.type;
    }

    public HirInstr getNext() {
        return next;
    }

    public void setNext(HirInstr next) {
        this.next = next;
    }

    public AllocatableValue loadOperandRaw() {
        return operand;
    }

    /**
     * Load operand directly
     *
     * @param visitor if operand is null, visitor this instruction by visitor
     * @return operand
     */
    public AllocatableValue loadOperand(HirInstrVisitor visitor) {
        if (operand == null) {
            this.visit(visitor);
        }
        YarrowError.guarantee(operand != null, "Must be not null");

        return operand;
    }

    /**
     * Load operand into new virtual register, new virtual register will allocate immediately
     *
     * @param visitor visitor if operand is null, visitor this instruction by visitor
     * @param gen     generate move instruction if needed
     * @return operand
     */
    public AllocatableValue loadOperandToReg(HirInstrVisitor visitor, LirGenerator gen) {
        if (operand == null) {
            this.visit(visitor);
        }
        YarrowError.guarantee(operand != null, "Must be not null");

        if (!(operand instanceof VirtualRegister)) {
            VirtualRegister register = new VirtualRegister(type);
            gen.emitMov(register, operand);
            operand = register;
        }
        YarrowError.guarantee(operand instanceof ConstValue || operand instanceof VirtualRegister, "Operand should retain in virtual register");

        return operand;
    }

    /**
     * Load operand into specific register
     *
     * @param visitor  visitor if operand is null, visitor this instruction by visitor
     * @param gen      generate move instruction if needed
     * @param register specific register
     * @return operand
     */
    public AllocatableValue loadOperandToReg(HirInstrVisitor visitor, LirGenerator gen, VirtualRegister register) {
        if (operand == null) {
            this.visit(visitor);
        }
        YarrowError.guarantee(operand != null, "Must be not null");

        if (operand != register) {
            gen.emitMov(register, operand);
            operand = register;
        }
        YarrowError.guarantee(operand instanceof VirtualRegister, "Operand should retain in virtual register");

        return operand;
    }

    public void storeOperand(AllocatableValue operand) {
        YarrowError.guarantee(this.operand == null, "The first installation");
        this.operand = operand;
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
     * @for HIR
     */
    public HirInstr ideal() {
        return this;
    }
}

