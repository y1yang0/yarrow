package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.CompilerErrors;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.common.JVMCIError;

public class ArithmeticInstr extends Op2Instr {
    public ArithmeticInstr(int opcode, Instruction left, Instruction right) {
        super(new Value(left.getType()), opcode, left, right);
        if (!right.isType(left.getType())) {
            throw new YarrowError("Incompatible operand type");
        }
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
                JVMCIError.shouldNotReachHere();
        }

        return Logger.format("i{}: i{} {} i{}", super.id, super.left.id, op, super.right.id);
    }
}
