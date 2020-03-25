package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;

import java.util.Objects;

public class NegateInstr extends HirInstr {
    private HirInstr left;

    public NegateInstr(HirInstr left) {
        super(left.type());
        this.left = left;
    }

    @Override
    public HirInstr ideal() {
        if (left instanceof ConstantInstr) {
            if (left.isType(JavaKind.Int)) {
                return new ConstantInstr(JavaConstant.forInt(-((ConstantInstr) left).getConstant().asInt()));
            } else if (left.isType(JavaKind.Long)) {
                return new ConstantInstr(JavaConstant.forLong(-((ConstantInstr) left).getConstant().asLong()));
            } else if (left.isType(JavaKind.Float)) {
                return new ConstantInstr(JavaConstant.forFloat(-((ConstantInstr) left).getConstant().asFloat()));
            } else if (left.isType(JavaKind.Double)) {
                return new ConstantInstr(JavaConstant.forDouble(-((ConstantInstr) left).getConstant().asDouble()));
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
