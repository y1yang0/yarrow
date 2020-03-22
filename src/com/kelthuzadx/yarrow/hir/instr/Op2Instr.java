package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;

import java.util.Objects;

public abstract class Op2Instr extends HirInstruction {
    protected int opcode;
    protected HirInstruction left;
    protected HirInstruction right;

    public HirInstruction getLeft() {
        return left;
    }

    public HirInstruction getRight() {
        return right;
    }

    public Op2Instr(Value value, int opcode, HirInstruction left, HirInstruction right) {
        super(value);
        this.opcode = opcode;
        this.left = left;
        this.right = right;
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
