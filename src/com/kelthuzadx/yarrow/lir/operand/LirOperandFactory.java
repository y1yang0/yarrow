package com.kelthuzadx.yarrow.lir.operand;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.ConstantInstr;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.lir.operand.OperandKind;
import com.kelthuzadx.yarrow.lir.operand.OperandType;
import jdk.vm.ci.meta.JavaKind;

public class LirOperandFactory {
    private static int vregisterId = 0;

    public static ConstOperand createConstInt(ConstantInstr instr){
        switch (instr.type()){
            case Int:
                return new ConstOperand(OperandType.Int,(int)instr.value());
            case Long:
                return new ConstOperand(OperandType.Long,(long)instr.value());
            case Float:
                return new ConstOperand(OperandType.Float,(float)instr.value());
            case Double:
                return new ConstOperand(OperandType.Double,(double)instr.value());
            default:
                YarrowError.shouldNotReachHere();
        }
        return null;
    }

    public static LirOperand createVirtualRegister(JavaKind type){
        switch (type){
            case Int:
                return new LirOperand(OperandKind.Register,OperandType.Int);
            case Long:
                return new LirOperand(OperandKind.Register,OperandType.Long);
            case Float:
                return new LirOperand(OperandKind.Register,OperandType.Float);
            case Double:
                return new LirOperand(OperandKind.Register,OperandType.Double);
            default:
                YarrowError.shouldNotReachHere();
        }
        return null;
    }
}
