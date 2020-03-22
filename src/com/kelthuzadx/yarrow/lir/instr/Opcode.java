package com.kelthuzadx.yarrow.lir.instr;

public enum Opcode {
    Illegal,

    // 1 operand opcode
    Label,
    MOV,
    TypeCast,

    // 2 operands opcode
    ADD,
    SUB,
    MUL,
    DIV,
    REM,
    // 3 operand opcode
}
