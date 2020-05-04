package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.hotspot.HotSpotObjectConstant;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;

import java.util.Objects;

public class ConstantInstr extends HirInstr {
    private final JavaConstant constant;

    public ConstantInstr(JavaConstant constant) {
        super(constant.getJavaKind());
        this.constant = constant;
    }

    public JavaConstant getConstant() {
        return constant;
    }

    @Override
    public String toString() {
        if (!isType(JavaKind.Illegal)) {

            if (constant.isNull()) {
                return Logger.format("i{}: nullptr", super.id);
            } else {
                if (constant instanceof HotSpotObjectConstant && ((HotSpotObjectConstant) constant).isInternedString()) {
                    return Logger.format("i{}: '{}'", super.id, constant.toValueString());
                } else {
                    return Logger.format("i{}: {}", super.id, constant.toValueString());
                }
            }
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstantInstr)) return false;
        var that = (ConstantInstr) o;
        return constant.equals(that.constant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constant);
    }
}
