package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.value.ValueType;

public class TypeCastInstr extends Instruction{
    private int opcode;
    private Instruction from;
    private ValueType toType;

    public TypeCastInstr(int opcode, Instruction from, ValueType toType) {
        this.opcode = opcode;
        this.from = from;
        this.toType = toType;
    }
}
