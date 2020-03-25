package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.PrimitiveConstant;

public class ArithmeticInstr extends Op2Instr {
    public ArithmeticInstr(int opcode, HirInstr left, HirInstr right) {
        super(left.type, opcode, left, right);
        if (!right.isType(left.type())) {
            throw new YarrowError("Incompatible operand type");
        }
    }

    @Override
    public HirInstr ideal() {
        if (left == right) {
            switch (opcode) {
                case Bytecode.ISUB:
                    return new ConstantInstr(PrimitiveConstant.INT_0);
                case Bytecode.LSUB:
                    return new ConstantInstr(PrimitiveConstant.LONG_0);
            }
        }

        if (left instanceof ConstantInstr && right instanceof ConstantInstr) {
            // i1: 1+2 -> i2: 3
            // i1: 1-2 -> i2: 1
            // i1: 1*2 -> i2: 2
            // i1: 1/2 -> i2: 0
            // i1: 1%2 -> i2: 1
            if (left.isType(JavaKind.Int)) {
                int x = ((ConstantInstr) left).getConstant().asInt();
                int y = ((ConstantInstr) right).getConstant().asInt();
                switch (opcode) {
                    case Bytecode.IADD:
                        return new ConstantInstr(JavaConstant.forInt(x+y));
                    case Bytecode.ISUB:
                        return new ConstantInstr(JavaConstant.forInt(x - y));
                    case Bytecode.IMUL:
                        return new ConstantInstr(JavaConstant.forInt(x * y));
                    case Bytecode.IDIV:
                        return new ConstantInstr(JavaConstant.forInt(x / y));
                    case Bytecode.IREM:
                        return new ConstantInstr(JavaConstant.forInt(x % y));
                    default:
                        YarrowError.shouldNotReachHere();
                }

            } else if (left.isType(JavaKind.Long)) {
                long x = ((ConstantInstr) left).getConstant().asLong();
                long y = ((ConstantInstr) right).getConstant().asLong();
                switch (opcode) {
                    case Bytecode.LADD:
                        return new ConstantInstr(JavaConstant.forLong(x + y));
                    case Bytecode.LSUB:
                        return new ConstantInstr(JavaConstant.forLong(x - y));
                    case Bytecode.LMUL:
                        return new ConstantInstr(JavaConstant.forLong(x * y));
                    case Bytecode.LDIV:
                        return new ConstantInstr(JavaConstant.forLong(x / y));
                    case Bytecode.LREM:
                        return new ConstantInstr(JavaConstant.forLong(x % y));
                    default:
                        YarrowError.shouldNotReachHere();
                }
            }
            // else{ ... }
            // As C1's sourcenote, I should must be extremely careful with floats
            // and double, so I give up ;-0
        }

        if (right instanceof ConstantInstr) {
            if (left.isType(JavaKind.Int) && (int) ((ConstantInstr) right).getConstant().asInt() == 0) {
                int x = ((ConstantInstr)left).getConstant().asInt();
                switch (opcode) {
                    case Bytecode.IADD:
                    case Bytecode.ISUB:
                        return new ConstantInstr(JavaConstant.forInt( x));
                    case Bytecode.IMUL:
                        return new ConstantInstr(JavaConstant.INT_0);
                    default:
                        YarrowError.shouldNotReachHere();
                }
            } else if (left.isType(JavaKind.Long)) {
                long x = ((ConstantInstr)left).getConstant().asLong();
                switch (opcode) {
                    case Bytecode.IADD:
                    case Bytecode.ISUB:
                        return new ConstantInstr(JavaConstant.forLong( x));
                    case Bytecode.IMUL:
                        return new ConstantInstr(JavaConstant.LONG_0);
                    default:
                        YarrowError.shouldNotReachHere();
                }
            }
        }

        return this;
    }

    @Override
    public String toString() {
        char op = '\0';
        switch (super.opcode) {
            case Bytecode.IADD:
            case Bytecode.LADD:
            case Bytecode.FADD:
            case Bytecode.DADD:
                op = '+';
                break;
            case Bytecode.ISUB:
            case Bytecode.LSUB:
            case Bytecode.FSUB:
            case Bytecode.DSUB:
                op = '-';
                break;
            case Bytecode.IMUL:
            case Bytecode.LMUL:
            case Bytecode.FMUL:
            case Bytecode.DMUL:
                op = '*';
                break;
            case Bytecode.IDIV:
            case Bytecode.LDIV:
            case Bytecode.FDIV:
            case Bytecode.DDIV:
                op = '/';
                break;
            case Bytecode.IREM:
            case Bytecode.LREM:
            case Bytecode.FREM:
            case Bytecode.DREM:
                op = '%';
                break;
            default:
                YarrowError.shouldNotReachHere();
        }

        return Logger.format("i{}: i{} {} i{}", super.id, super.left.id, op, super.right.id);
    }
}
