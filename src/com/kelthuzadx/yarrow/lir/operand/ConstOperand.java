package com.kelthuzadx.yarrow.lir.operand;

public class ConstOperand<T> extends PtrOperand {
    private T constValue;
    public ConstOperand(OperandType type, T constValue) {
        super(OperandKind.Constant, type);
        this.constValue = constValue;
    }
}
