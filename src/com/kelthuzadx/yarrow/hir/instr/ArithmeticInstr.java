package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

public class ArithmeticInstr extends Op2Instr {
    public ArithmeticInstr(int opcode, Instruction left, Instruction right){
        super(new Value(left.getType()), opcode,left,right);
        if(!right.isType(left.getType())){
            throw new YarrowError("Incompatible operand type");
        }
    }
}
