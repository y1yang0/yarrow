package com.kelthuzadx.yarrow.lir.instr;

public enum Opcode {
    Illegal,
    // 0 operand opcode
    NormalEntry,
    OsrEntry,

    // 1 operand opcode
    Label,
    MOV,
    TypeCast,
    JMP,

    // 2 operands opcode
    ADD,
    SUB,
    MUL,
    DIV,
    REM,
    // 3 operand opcode
}
