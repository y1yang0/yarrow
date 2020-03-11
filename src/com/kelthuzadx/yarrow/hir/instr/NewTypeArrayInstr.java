package com.kelthuzadx.yarrow.hir.instr;

public class NewTypeArrayInstr extends Instruction {
    private Instruction len;
    private ValueType type;

    public NewTypeArrayInstr(Instruction len, ValueType type) {
        this.len = len;
        this.type = type;
    }
}
