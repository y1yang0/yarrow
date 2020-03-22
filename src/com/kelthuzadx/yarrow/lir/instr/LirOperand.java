package com.kelthuzadx.yarrow.lir.instr;

public class LirOperand {
    private static LirOperand illegal = new LirOperand();
    private OperandType type;

    public static LirOperand createIllegal(){
        return illegal;
    }
}
