package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class LogicInstr extends Op2Instr {
    public LogicInstr(int opcode, Instruction left, Instruction right) {
        super(new Value(left.type()), opcode, left, right);
        if (!right.isType(left.type())) {
            throw new YarrowError("Incompatible operand type");
        }
    }

    @Override
    public Instruction ideal() {
        if (left == right) {
            switch (opcode) {
                case Bytecode.IXOR:
                    return new ConstantInstr(new Value(JavaKind.Int, 0));
                case Bytecode.LXOR:
                    return new ConstantInstr(new Value(JavaKind.Long, 0L));
            }
        }

        if (left instanceof ConstantInstr && right instanceof ConstantInstr) {
            // i1: 1&2 -> i2: 0
            // i1: 1|2 -> i2: 3
            // i1: 1^2 -> i2: 3
            if (left.isType(JavaKind.Int)) {
                int x = left.value();
                int y = right.value();
                switch (opcode) {
                    case Bytecode.IAND:
                        return new ConstantInstr(new Value(JavaKind.Int, x & y));
                    case Bytecode.IOR:
                        return new ConstantInstr(new Value(JavaKind.Int, x | y));
                    case Bytecode.IXOR:
                        return new ConstantInstr(new Value(JavaKind.Int, x ^ y));
                    default:
                        YarrowError.shouldNotReachHere();
                }

            } else if (left.isType(JavaKind.Long)) {
                long x = left.value();
                long y = right.value();
                switch (opcode) {
                    case Bytecode.LAND:
                        return new ConstantInstr(new Value(JavaKind.Long, x & y));
                    case Bytecode.LOR:
                        return new ConstantInstr(new Value(JavaKind.Long, x | y));
                    case Bytecode.LXOR:
                        return new ConstantInstr(new Value(JavaKind.Long, x ^ y));
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
                    case Bytecode.IAND:
                        return new ConstantInstr(new Value(JavaKind.Int, 0));
                    case Bytecode.IOR:
                        return new ConstantInstr(new Value(JavaKind.Int, x));
                    default:
                        YarrowError.shouldNotReachHere();
                }
            } else if (left.isType(JavaKind.Long)) {
                long x = left.value();
                switch (opcode) {
                    case Bytecode.IADD:
                    case Bytecode.ISUB:
                        return new ConstantInstr(new Value(JavaKind.Long, 0L));
                    case Bytecode.IMUL:
                        return new ConstantInstr(new Value(JavaKind.Long, x));
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
