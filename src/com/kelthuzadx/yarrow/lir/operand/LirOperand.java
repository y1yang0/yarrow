package com.kelthuzadx.yarrow.lir.operand;

import java.util.Optional;

public class LirOperand {
    private final OperandKind kind;
    private final OperandType type;

    private final Optional<Object> constValue;
    private final Optional<Integer> vregId;

    public LirOperand(OperandKind kind, OperandType type) {
        this.kind = kind;
        this.type = type;
        this.constValue = Optional.empty();
        this.vregId = Optional.empty();
    }

    public LirOperand(OperandKind kind, OperandType type, int vregId) {
        this.kind = kind;
        this.type = type;
        this.constValue = Optional.empty();
        this.vregId = Optional.ofNullable(vregId);
    }

    public LirOperand(OperandKind kind, OperandType type, Object constValue) {
        this.kind = kind;
        this.type = type;
        this.constValue = Optional.ofNullable(constValue);
        this.vregId = Optional.empty();
    }

    public OperandType type() {
        return type;
    }

    public OperandKind kind() {
        return kind;
    }
}
