package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;

public class CompareHirInstr extends Op2HirInstr {
    public CompareHirInstr(int opcode, HirInstr left, HirInstr right) {
        super(JavaKind.Int, opcode, left, right);
    }

    @Override
    public HirInstr ideal() {
        if (left instanceof ConstantInstr && right instanceof ConstantInstr) {
            if (left.isType(JavaKind.Long) && right.isType(JavaKind.Long)) {
                long x = ((ConstantInstr) left).getConstant().asLong();
                long y = ((ConstantInstr) right).getConstant().asLong();
                if (x > y) {
                    return new ConstantInstr(JavaConstant.INT_1);
                } else if (x == y) {
                    return new ConstantInstr(JavaConstant.INT_0);
                } else {
                    return new ConstantInstr(JavaConstant.INT_MINUS_1);
                }
            } else if (left.isType(JavaKind.Float) && right.isType(JavaKind.Float)) {
                float x = ((ConstantInstr) left).getConstant().asFloat();
                float y = ((ConstantInstr) right).getConstant().asFloat();
                // at least one of value1' or value2' is NaN. The fcmpg instruction pushes the int value 1
                // onto the operand stack and the fcmpl pushes the int value -1 onto the operand stack.
                if (Float.isNaN(x) || Float.isNaN(y)) {
                    if (opcode == Bytecode.FCMPL) {
                        return new ConstantInstr(JavaConstant.INT_MINUS_1);
                    } else if (opcode == Bytecode.FCMPG) {
                        return new ConstantInstr(JavaConstant.INT_1);
                    } else {
                        YarrowError.shouldNotReachHere();
                    }
                }
                if (x > y) {
                    return new ConstantInstr(JavaConstant.INT_1);
                } else if (x == y) {
                    return new ConstantInstr(JavaConstant.INT_0);
                } else if (x < y) {
                    return new ConstantInstr(JavaConstant.INT_MINUS_1);
                }
            } else if (left.isType(JavaKind.Double) && right.isType(JavaKind.Double)) {
                double x = ((ConstantInstr) left).getConstant().asDouble();
                double y = ((ConstantInstr) right).getConstant().asDouble();
                // ditto
                if (Double.isNaN(x) || Double.isNaN(y)) {
                    if (opcode == Bytecode.DCMPL) {
                        return new ConstantInstr(JavaConstant.INT_MINUS_1);
                    } else if (opcode == Bytecode.FCMPG) {
                        return new ConstantInstr(JavaConstant.INT_1);
                    } else {
                        YarrowError.shouldNotReachHere();
                    }
                }
                if (x > y) {
                    return new ConstantInstr(JavaConstant.INT_1);
                } else if (x == y) {
                    return new ConstantInstr(JavaConstant.INT_0);
                } else if (x < y) {
                    return new ConstantInstr(JavaConstant.INT_MINUS_1);
                }
            }
        }

        return this;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: cmp i{},i{}", super.id, super.left.id, super.right.id);
    }
}
