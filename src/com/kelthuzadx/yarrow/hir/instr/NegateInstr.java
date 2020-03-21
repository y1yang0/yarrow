package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

import java.util.Objects;

public class NegateInstr extends HirInstruction {
    private HirInstruction left;

    public NegateInstr(HirInstruction left) {
        super(new Value(left.type()));
        this.left = left;
    }

    @Override
    public HirInstruction ideal() {
        if (left instanceof ConstantInstr) {
            if (left.isType(JavaKind.Int)) {
                return new ConstantInstr(new Value(JavaKind.Int, -((int) left.value())));
            } else if (left.isType(JavaKind.Long)) {
                return new ConstantInstr(new Value(JavaKind.Long, -((long) left.value())));
            } else if (left.isType(JavaKind.Float)) {
                return new ConstantInstr(new Value(JavaKind.Float, -((float) left.value())));
            } else if (left.isType(JavaKind.Double)) {
                return new ConstantInstr(new Value(JavaKind.Double, -((double) left.value())));
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: -i{}", super.id, left.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NegateInstr)) return false;
        var that = (NegateInstr) o;
        return left.equals(that.left);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left);
    }
}
