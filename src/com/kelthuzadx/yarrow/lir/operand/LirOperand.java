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

    @Override
    public String toString() {
        String k = "";
        switch (kind) {
            case Register:
                k = "R" + vregId.get();
                break;
            case Constant:
                k += constValue.get();
                break;
            case Stack:
                k = "S";
                break;
            case Address:
                k = "&";
                break;
        }
        String t = "";
        switch (type) {
            case Int:
                t = "I";
                break;
            case Long:
                t = "L";
                break;
            case Object:
                t = "A";
                break;
            case Address:
                t = "*";
                break;
            case Float:
                t = "F";
                break;
            case Double:
                t = "D";
                break;
            case Unknown:
                t = "?";
                break;
        }
        return k + ":" + t;
    }
}
