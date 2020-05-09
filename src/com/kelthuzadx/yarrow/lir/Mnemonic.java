package com.kelthuzadx.yarrow.lir;

public enum Mnemonic {
    ILLEGAL,
    NORMAL_ENTRY,
    OSR_ENTRY,
    LABEL,
    ALLOCATE_ARRAY,
    MEMBAR,
    MEMBAR_LOAD_LOAD,
    MEMBAR_LOAD_STORE,
    MEMBAR_STORE_STORE,
    MEMBAR_STORE_LOAD,
    MEMBAR_ACQUIRE,
    MEMBAR_RELEASE,
    CALL_RT,
    TYPE_CAST,
    INSTANCE_OF,
    CHECK_CAST,
    CALL_STATIC,
    CALL_VIRTUAL,
    CALL_OPTVIRTUAL,
    CALL_ICVIRTUAL,
    CALL_DYNAMIC,

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
