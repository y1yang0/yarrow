package com.kelthuzadx.yarrow.lir.operand;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.ConstantInstr;
import jdk.vm.ci.meta.JavaKind;

public class LirOperandFactory {
    private static int vregisterId = 0;

    public static LirOperand createIllegal() {
        return new LirOperand(OperandKind.Illegal, OperandType.Unknown);
    }


    public static LirOperand createConstInt(ConstantInstr instr) {
        switch (instr.type()) {
            case Int:
                return new LirOperand(OperandKind.Constant, OperandType.Int, instr.value());
            case Long:
                return new LirOperand(OperandKind.Constant, OperandType.Long, instr.value());
            case Float:
                return new LirOperand(OperandKind.Constant, OperandType.Float, instr.value());
            case Double:
                return new LirOperand(OperandKind.Constant, OperandType.Double, instr.value());
            default:
                YarrowError.shouldNotReachHere();
        }
        return null;
    }

    public static LirOperand createVirtualRegister(JavaKind type) {
        switch (type) {
            case Int:
                return new LirOperand(OperandKind.Register, OperandType.Int, vregisterId++);
            case Long:
                return new LirOperand(OperandKind.Register, OperandType.Long, vregisterId++);
            case Float:
                return new LirOperand(OperandKind.Register, OperandType.Float, vregisterId++);
            case Double:
                return new LirOperand(OperandKind.Register, OperandType.Double, vregisterId++);
            default:
                YarrowError.shouldNotReachHere();
        }
        return null;
    }
}
