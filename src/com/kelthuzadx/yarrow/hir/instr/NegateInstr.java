package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;

import java.util.Objects;

public class NegateInstr extends HirInstr {
    private final HirInstr value;

    public NegateInstr(HirInstr value) {
        super(value.type());
        this.value = value;
    }

    public HirInstr getValue() {
        return value;
    }

    @Override
    public HirInstr ideal() {
        if (value instanceof ConstantInstr) {
            if (value.isType(JavaKind.Int)) {
                return new ConstantInstr(JavaConstant.forInt(-((ConstantInstr) value).getConstant().asInt()));
            } else if (value.isType(JavaKind.Long)) {
                return new ConstantInstr(JavaConstant.forLong(-((ConstantInstr) value).getConstant().asLong()));
            } else if (value.isType(JavaKind.Float)) {
                return new ConstantInstr(JavaConstant.forFloat(-((ConstantInstr) value).getConstant().asFloat()));
            } else if (value.isType(JavaKind.Double)) {
                return new ConstantInstr(JavaConstant.forDouble(-((ConstantInstr) value).getConstant().asDouble()));
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: -i{}", super.id, value.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NegateInstr)) return false;
        var that = (NegateInstr) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
