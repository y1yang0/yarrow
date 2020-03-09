package com.kelthuzadx.yarrow.hir.instr;

public class ArithmeticInstr extends Op2Instr {
    public ArithmeticInstr(int opcode, Instruction left, Instruction right){
        super(opcode,left,right);
    }
}
