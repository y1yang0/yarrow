package com.kelthuzadx.yarrow.lir.operand;

import com.kelthuzadx.yarrow.core.YarrowError;

import java.util.Optional;

public class LirOperand {
    private OperandKind kind;
    private OperandType type;

    public LirOperand(OperandKind kind,OperandType type){
        this.kind = kind;
        this.type = type;
    }
}
