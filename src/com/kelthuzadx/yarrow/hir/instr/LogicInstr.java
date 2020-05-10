package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;

public class LogicInstr extends Op2HirInstr {
    public LogicInstr(int opcode, HirInstr left, HirInstr right) {
        super(left.type, opcode, left, right);
        if (!right.isType(left.type())) {
            throw new YarrowError("Incompatible operand type");
        }
    }

    @Override
    public HirInstr ideal() {
        if (left == right) {
            switch (opcode) {
                case Bytecode.IXOR:
                    return new ConstantInstr(JavaConstant.INT_0);
                case Bytecode.LXOR:
                    return new ConstantInstr(JavaConstant.LONG_0);
            }
        }

        if (left instanceof ConstantInstr && right instanceof ConstantInstr) {
            // i1: 1&2 -> i2: 0
            // i1: 1|2 -> i2: 3
            // i1: 1^2 -> i2: 3
            if (left.isType(JavaKind.Int)) {
                int x = ((ConstantInstr) left).getConstant().asInt();
                int y = ((ConstantInstr) right).getConstant().asInt();
                switch (opcode) {
                    case Bytecode.IAND:
                        return new ConstantInstr(JavaConstant.forInt(x & y));
                    case Bytecode.IOR:
                        return new ConstantInstr(JavaConstant.forInt(x | y));
                    case Bytecode.IXOR:
                        return new ConstantInstr(JavaConstant.forInt(x ^ y));
                    default:
                        YarrowError.shouldNotReachHere();
                }

            } else if (left.isType(JavaKind.Long)) {
                long x = ((ConstantInstr) left).getConstant().asLong();
                long y = ((ConstantInstr) right).getConstant().asLong();
                switch (opcode) {
                    case Bytecode.LAND:
                        return new ConstantInstr(JavaConstant.forLong(x & y));
                    case Bytecode.LOR:
                        return new ConstantInstr(JavaConstant.forLong(x | y));
                    case Bytecode.LXOR:
                        return new ConstantInstr(JavaConstant.forLong(x ^ y));
                    default:
                        YarrowError.shouldNotReachHere();
                }
            }
            // else{ ... }
            // As C1's sourcenote, I should must be extremely careful with floats
            // and double, so I give up ;-0
        }

        if (right instanceof ConstantInstr) {
            if (left.isType(JavaKind.Int) && ((ConstantInstr) right).getConstant().asInt() == 0) {
                int x = ((ConstantInstr) left).getConstant().asInt();
                switch (opcode) {
                    case Bytecode.IAND:
                        return new ConstantInstr(JavaConstant.INT_0);
                    case Bytecode.IOR:
                        return new ConstantInstr(JavaConstant.forInt(x));
                    default:
                        YarrowError.shouldNotReachHere();
                }
            } else if (left.isType(JavaKind.Long)) {
                long x = ((ConstantInstr) left).getConstant().asLong();
                switch (opcode) {
                    case Bytecode.IADD:
                    case Bytecode.ISUB:
                        return new ConstantInstr(JavaConstant.LONG_1);
                    case Bytecode.IMUL:
                        return new ConstantInstr(JavaConstant.forLong(x));
                    default:
                        YarrowError.shouldNotReachHere();
                }
            }
        }

        return this;
    }

    @Override
    public String toString() {
        String op = "";
        switch (super.opcode) {
            case Bytecode.IAND:
            case Bytecode.LAND:
                op = "&";
                break;
            case Bytecode.IOR:
            case Bytecode.LOR:
                op = "|";
                break;
            case Bytecode.IXOR:
            case Bytecode.LXOR:
                op = "^";
                break;
        }
        return Logger.format("i{}: i{} {} i{}", super.id, left.id, op, right.id);
    }
}
