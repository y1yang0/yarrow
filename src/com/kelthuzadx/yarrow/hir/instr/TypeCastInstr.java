package com.kelthuzadx.yarrow.hir.instr;

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
