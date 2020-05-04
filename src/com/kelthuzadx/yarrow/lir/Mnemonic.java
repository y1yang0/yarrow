package com.kelthuzadx.yarrow.lir;

public enum Mnemonic {
    Illegal,
    NormalEntry,
    OsrEntry,
    Label,
    AllocateArray,
    Membar,
    MembarLoadLoad,
    MembarLoadStore,
    MembarStoreStore,
    MembarStoreLoad,
    MembarAcquire,
    MembarRelease,
    CallRt,
    TypeCast,
    InstanceOf,
    CheckCast,
    Branch,

    // 1 operand opcode
    MOV,
    BRANCH,
    RETURN,

    // 2 operands opcode
    ADD,
    SUB,
    MUL,
    DIV,
    REM,
    SHL,
    SHR,
    USHR,
    AND,
    OR,
    XOR,
    NEG,
    FCMP,
    FCMPU,
    LCMP,
    CMP
    // 3 operand opcode
}
