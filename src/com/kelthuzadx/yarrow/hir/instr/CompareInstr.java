package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class CompareInstr extends Op2Instr {
    public CompareInstr(int opcode, Instruction left, Instruction right) {
        super(new Value(JavaKind.Int), opcode, left, right);
    }

    @Override
    public Instruction ideal() {
        if (left instanceof ConstantInstr && right instanceof ConstantInstr) {
            if (left.isType(JavaKind.Long) && right.isType(JavaKind.Long)) {
                long x = left.value();
                long y = right.value();
                if (x > y) {
                    return new ConstantInstr(new Value(JavaKind.Int, 1));
                } else if (x == y) {
                    return new ConstantInstr(new Value(JavaKind.Int, 0));
                } else {
                    return new ConstantInstr(new Value(JavaKind.Int, -1));
                }
            } else if (left.isType(JavaKind.Float) && right.isType(JavaKind.Float)) {
                float x = left.value();
                float y = right.value();
                // at least one of value1' or value2' is NaN. The fcmpg instruction pushes the int value 1
                // onto the operand stack and the fcmpl pushes the int value -1 onto the operand stack.
                if (Float.isNaN(x) || Float.isNaN(y)) {
                    if (opcode == Bytecode.FCMPL) {
                        return new ConstantInstr(new Value(JavaKind.Int, -1));
                    } else if (opcode == Bytecode.FCMPG) {
                        return new ConstantInstr(new Value(JavaKind.Int, 1));
                    } else {
                        YarrowError.shouldNotReachHere();
                    }
                }
                if (x > y) {
                    return new ConstantInstr(new Value(JavaKind.Int, 1));
                } else if (x == y) {
                    return new ConstantInstr(new Value(JavaKind.Int, 0));
                } else if (x < y) {
                    return new ConstantInstr(new Value(JavaKind.Int, -1));
                }
            } else if (left.isType(JavaKind.Double) && right.isType(JavaKind.Double)) {
                double x = left.value();
                double y = right.value();
                // ditto
                if (Double.isNaN(x) || Double.isNaN(y)) {
                    if (opcode == Bytecode.DCMPL) {
                        return new ConstantInstr(new Value(JavaKind.Int, -1));
                    } else if (opcode == Bytecode.FCMPG) {
                        return new ConstantInstr(new Value(JavaKind.Int, 1));
                    } else {
                        YarrowError.shouldNotReachHere();
                    }
                }
                if (x > y) {
                    return new ConstantInstr(new Value(JavaKind.Int, 1));
                } else if (x == y) {
                    return new ConstantInstr(new Value(JavaKind.Int, 0));
                } else if (x < y) {
                    return new ConstantInstr(new Value(JavaKind.Int, -1));
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
