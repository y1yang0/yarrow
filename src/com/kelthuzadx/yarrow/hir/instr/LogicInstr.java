package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;

public class LogicInstr extends Op2Instr {
    public LogicInstr(int opcode, Instruction left, Instruction right) {
        super(new Value(left.getType()),opcode, left, right);
        if(!right.isType(left.getType())){
            throw new YarrowError("Incompatible operand type");
        }
    }
}
