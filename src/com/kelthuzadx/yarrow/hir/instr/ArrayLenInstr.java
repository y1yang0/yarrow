package com.kelthuzadx.yarrow.hir.instr;

public class ArrayLenInstr extends Instruction {
    private Instruction array;

    public ArrayLenInstr(Instruction array){
        this.array=array;
    }
}
