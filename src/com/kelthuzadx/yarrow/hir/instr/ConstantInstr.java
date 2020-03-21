package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

import java.util.Objects;

public class ConstantInstr extends HirInstruction {
    public ConstantInstr(Value value) {
        super(value);
    }


    @Override
    public String toString() {
        if (!isType(JavaKind.Illegal)) {

            if (value() == null) {
                return Logger.format("i{}: nullptr", super.id);
            } else {
                var val = value();
                if (val instanceof String) {
                    return Logger.format("i{}: '{}'", super.id, val);
                } else {
                    return Logger.format("i{}: {}", super.id, val);
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
        switch (type()) {
            case Boolean:
                return (boolean) value() == (boolean) that.value();
            case Byte:
                return (byte) value() == (byte) that.value();
            case Short:
                return (short) value() == (short) that.value();
            case Char:
                return (char) value() == (char) that.value();
            case Int:
                return (int) value() == (int) that.value();
            case Float:
                return (float) value() == (float) that.value();
            case Long:
                return (long) value() == (long) that.value();
            case Double:
                return (double) value() == (double) that.value();
            case Object:
                return value().equals(that.value());
            case Void:
            case Illegal:
            default:
                break;
        }
        return false;
    }

    @Override
    public int hashCode() {
        var v = value();
        return Objects.hash(v);
    }
}
