package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class ArithmeticInstr extends Op2Instr {
    public ArithmeticInstr(int opcode, Instruction left, Instruction right) {
        super(new Value(left.type()), opcode, left, right);
        if (!right.isType(left.type())) {
            throw new YarrowError("Incompatible operand type");
        }
    }

    @Override
    public Instruction ideal() {
        if (left == right) {
            switch (opcode) {
                case Bytecode.ISUB:
                    return new ConstantInstr(new Value(JavaKind.Int, 0));
                case Bytecode.LSUB:
                    return new ConstantInstr(new Value(JavaKind.Long, 0L));
            }
        }

        if (left instanceof ConstantInstr && right instanceof ConstantInstr) {
            // i1: 1+2 -> i2: 3
            // i1: 1-2 -> i2: 1
            // i1: 1*2 -> i2: 2
            // i1: 1/2 -> i2: 0
            // i1: 1%2 -> i2: 1
            if (left.isType(JavaKind.Int)) {
                int x = left.value();
                int y = right.value();
                switch (opcode) {
                    case Bytecode.IADD:
                        return new ConstantInstr(new Value(JavaKind.Int, x + y));
                    case Bytecode.ISUB:
                        return new ConstantInstr(new Value(JavaKind.Int, x - y));
                    case Bytecode.IMUL:
                        return new ConstantInstr(new Value(JavaKind.Int, x * y));
                    case Bytecode.IDIV:
                        return new ConstantInstr(new Value(JavaKind.Int, x / y));
                    case Bytecode.IREM:
                        return new ConstantInstr(new Value(JavaKind.Int, x % y));
                    default:
                        YarrowError.shouldNotReachHere();
                }

            } else if (left.isType(JavaKind.Long)) {
                long x = left.value();
                long y = right.value();
                switch (opcode) {
                    case Bytecode.LADD:
                        return new ConstantInstr(new Value(JavaKind.Long, x + y));
                    case Bytecode.LSUB:
                        return new ConstantInstr(new Value(JavaKind.Long, x - y));
                    case Bytecode.LMUL:
                        return new ConstantInstr(new Value(JavaKind.Long, x * y));
                    case Bytecode.LDIV:
                        return new ConstantInstr(new Value(JavaKind.Long, x / y));
                    case Bytecode.LREM:
                        return new ConstantInstr(new Value(JavaKind.Long, x % y));
                    default:
                        YarrowError.shouldNotReachHere();
                }
            }
            // else{ ... }
            // As C1's sourcenote, I should must be extremely careful with floats
            // and double, so I give up ;-0
        }

        if (right instanceof ConstantInstr) {
            if (left.isType(JavaKind.Int) && (int) right.value() == 0) {
                int x = left.value();
                switch (opcode) {
                    case Bytecode.IADD:
                    case Bytecode.ISUB:
                        return new ConstantInstr(new Value(JavaKind.Int, x));
                    case Bytecode.IMUL:
                        return new ConstantInstr(new Value(JavaKind.Int, 0));
                    default:
                        YarrowError.shouldNotReachHere();
                }
            } else if (left.isType(JavaKind.Long)) {
                long x = left.value();
                switch (opcode) {
                    case Bytecode.IADD:
                    case Bytecode.ISUB:
                        return new ConstantInstr(new Value(JavaKind.Long, x));
                    case Bytecode.IMUL:
                        return new ConstantInstr(new Value(JavaKind.Long, 0L));
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
