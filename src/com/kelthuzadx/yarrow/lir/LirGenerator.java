package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.opcode.*;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;


/**
 * Stateful low level IR generator
 */
public class LirGenerator {
    private Lir lir;
    private int currentBlockId;

    public LirGenerator(Lir lir) {
        this.lir = lir;
        this.currentBlockId = -1;
    }

    public void setCurrentBlockId(int currentBlockId) {
        this.currentBlockId = currentBlockId;
    }

    public void emitJavaCast(LirOperand result, LirOperand operand, int bytecode) {
        appendToList(new JavaCastInstr(result, operand, bytecode));
    }

    public void emitAdd(LirOperand result, LirOperand left, LirOperand right) {
        appendToList(new Op2Instr(Mnemonic.ADD, result, left, right));
    }

    public void emitSub(LirOperand result, LirOperand left, LirOperand right) {
        appendToList(new Op2Instr(Mnemonic.SUB, result, left, right));
    }

    public void emitMul(LirOperand result, LirOperand left, LirOperand right) {
        appendToList(new Op2Instr(Mnemonic.MUL, result, left, right));
    }

    public void emitDiv(LirOperand result, LirOperand left, LirOperand right) {
        appendToList(new Op2Instr(Mnemonic.DIV, result, left, right));
    }

    public void emitRem(LirOperand result, LirOperand left, LirOperand right) {
        appendToList(new Op2Instr(Mnemonic.REM, result, left, right));
    }

    public void emitMov(LirOperand dest, LirOperand src) {
        appendToList(new Op1Instr(Mnemonic.MOV, dest, src));
    }

    public void emitMembar(Mnemonic mnemonic) {
        appendToList(new Op0Instr(mnemonic, LirOperand.illegal));
    }

    public void emitJmp(BlockStartInstr block) {
        appendToList(new JmpInstr(Cond.Always, block));
    }

    public void emitReturn(LirOperand ret) {
        appendToList(new Op1Instr(Mnemonic.RETURN, LirOperand.illegal, ret));
    }

    public void emitNormalEntry() {
        appendToList(new Op0Instr(Mnemonic.NormalEntry, LirOperand.illegal));
    }

    public void emitOsrEntry() {
        appendToList(new Op0Instr(Mnemonic.OsrEntry, LirOperand.illegal));
    }

    private void appendToList(LirInstr instr) {
        lir.appendLirInstr(currentBlockId, instr);
    }
}
