package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;

import java.util.Optional;

public class LirOperand {
    private OperandType type;
    private Optional<Value> constValue;

    public LirOperand(OperandType type){
        this.type = type;
    }
    public LirOperand(OperandType type, Value constValue){
        YarrowError.guarantee(type==OperandType.Constant,"Must be a constant");
        this.type = type;
        this.constValue = Optional.ofNullable(constValue);
    }
}
