package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

import java.util.Objects;

public abstract class Op2Instr extends HirInstr {
    protected int opcode;
    protected HirInstr left;
    protected HirInstr right;

    public Op2Instr(JavaKind type, int opcode, HirInstr left, HirInstr right) {
        super(type);
        this.opcode = opcode;
        this.left = left;
        this.right = right;
    }

    public HirInstr getLeft() {
        return left;
    }

    public HirInstr getRight() {
        return right;
    }

    public int getOpcode() {
        return opcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Op2Instr)) return false;
        var that = (Op2Instr) o;
        return opcode == that.opcode && left.equals(that.left) && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, opcode);
    }
}
