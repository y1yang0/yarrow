package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.instr.*;
import com.kelthuzadx.yarrow.lir.operand.Address;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.lir.stub.ClassCastExStub;
import com.kelthuzadx.yarrow.lir.stub.NewArrayStub;
import com.kelthuzadx.yarrow.lir.stub.RuntimeStub;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaType;
import jdk.vm.ci.meta.JavaKind;

import java.util.concurrent.locks.Condition;

import static com.kelthuzadx.yarrow.core.YarrowProperties.Debug.TraceLIRGeneration;


/**
 * Stateful low level IR generator
 */
public class LirGenerator {
    private final Lir lir;
    private int currentBlockId;

    public LirGenerator(Lir lir) {
        this.lir = lir;
        this.currentBlockId = -1;
    }

    public void setCurrentBlockId(int currentBlockId) {
        this.currentBlockId = currentBlockId;
    }

    public void emitCmp(LirOperand left, LirOperand right, Cond cond){
        appendToList(new Op2Instr(Mnemonic.CMP,cond,LirOperand.illegal,left,right));
    }

    public void emitCheckCast(LirOperand result, LirOperand object, HotSpotResolvedJavaType klassType, ClassCastExStub stub) {
        appendToList(new JavaCheckCastInstr(result, object, klassType, stub));
    }

    public void emitInstanceOf(LirOperand result, LirOperand object, HotSpotResolvedJavaType klass) {
        appendToList(new JavaInstanceOfInstr(result, object, klass));
    }

    public void emitLcmp(LirOperand result, LirOperand left, LirOperand right) {
        appendToList(new Op2Instr(Mnemonic.LCMP, result, left, right));
    }

    public void emitFcmp(LirOperand result, LirOperand left, LirOperand right, boolean isUnorderedLess) {
        appendToList(new Op2Instr(isUnorderedLess ? Mnemonic.FCMPU : Mnemonic.FCMP, result, left, right));
    }

    public void emitAllocateArray(NewArrayStub stub, LirOperand klassReg, LirOperand dest, LirOperand len, LirOperand temp1, LirOperand temp2, LirOperand temp3, LirOperand temp4, JavaKind elementType) {
        appendToList(new AllocateArrayInstr(stub, klassReg, dest, len, temp1, temp2, temp3, temp4, elementType));
    }

    public void emitLabel(LabelInstr labelInstr) {
        appendToList(labelInstr);
    }

    public void emitNeg(LirOperand from, LirOperand to) {
        appendToList(new Op2Instr(Mnemonic.NEG, to, from, LirOperand.illegal));
    }

    public void emitAnd(LirOperand dest, LirOperand left, LirOperand right) {
        appendToList(new Op2Instr(Mnemonic.AND, dest, left, right));
    }

    public void emitOr(LirOperand dest, LirOperand left, LirOperand right) {
        appendToList(new Op2Instr(Mnemonic.OR, dest, left, right));
    }

    public void emitXor(LirOperand dest, LirOperand left, LirOperand right) {
        appendToList(new Op2Instr(Mnemonic.XOR, dest, left, right));
    }

    public void emitShl(LirOperand dest, LirOperand value, LirOperand count) {
        appendToList(new Op2Instr(Mnemonic.SHL, dest, value, count));
    }

    public void emitShr(LirOperand dest, LirOperand value, LirOperand count) {
        appendToList(new Op2Instr(Mnemonic.SHR, dest, value, count));
    }

    public void emitUshr(LirOperand dest, LirOperand value, LirOperand count) {
        appendToList(new Op2Instr(Mnemonic.USHR, dest, value, count));
    }

    public void emitJavaCast(LirOperand result, LirOperand operand, int bytecode) {
        appendToList(new JavaTypeCastInstr(result, operand, bytecode));
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

    public void emitCallRt(LirOperand result, Address rountine, LirOperand[] argument) {
        appendToList(new CallRtInstr(result, rountine, argument));
    }

    public void emitMembar(Mnemonic mnemonic) {
        appendToList(new Op0Instr(mnemonic, LirOperand.illegal));
    }

    public void emitJmp(BlockStartInstr block) {
        appendToList(new BranchInstr(Cond.Always, block));
    }

    public void emitJmp(RuntimeStub stub) {
        appendToList(new BranchInstr(Cond.Always, stub));
    }

    public void emitBranch(Cond condition, JavaKind type, BlockStartInstr block){
        appendToList(new BranchInstr(condition,type,block));
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
        if (TraceLIRGeneration) {
            Logger.logf("{}", instr.toString());
        }
        lir.appendLirInstr(currentBlockId, instr);
    }
}
