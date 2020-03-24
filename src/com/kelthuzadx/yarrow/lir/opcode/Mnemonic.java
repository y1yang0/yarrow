package com.kelthuzadx.yarrow.lir.opcode;

public enum Mnemonic {
    Illegal,
    // 0 operand opcode
    NormalEntry,
    OsrEntry,
    Membar,
    MembarLoadLoad,
    MembarLoadStore,
    MembarStoreStore,
    MembarStoreLoad,
    MembarAcquire,
    MembarRelease,

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
