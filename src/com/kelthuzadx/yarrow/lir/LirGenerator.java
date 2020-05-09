package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.instr.*;
import com.kelthuzadx.yarrow.lir.operand.Address;
import com.kelthuzadx.yarrow.lir.operand.ConstValue;
import com.kelthuzadx.yarrow.lir.stub.ClassCastExStub;
import com.kelthuzadx.yarrow.lir.stub.NewArrayStub;
import com.kelthuzadx.yarrow.lir.stub.RuntimeStub;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaType;
import jdk.vm.ci.meta.AllocatableValue;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaMethod;

import static com.kelthuzadx.yarrow.core.YarrowProperties.Debug.TraceLIRGeneration;


/**
 * Stateful low level IR generator
 *
 * @author kelthuzadx
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

    public void emitCmp(AllocatableValue left, int right, Cond cond) {
        appendToList(new Op2Instr(Mnemonic.CMP, cond, AllocatableValue.ILLEGAL, left, new ConstValue(JavaConstant.forInt(right))));
    }

    public void emitCmp(AllocatableValue left, AllocatableValue right, Cond cond) {
        appendToList(new Op2Instr(Mnemonic.CMP, cond, AllocatableValue.ILLEGAL, left, right));
    }

    public void emitCheckCast(AllocatableValue result, AllocatableValue object, HotSpotResolvedJavaType klassType, ClassCastExStub stub) {
        appendToList(new JavaCheckCastInstr(result, object, klassType, stub));
    }

    public void emitInstanceOf(AllocatableValue result, AllocatableValue object, HotSpotResolvedJavaType klass) {
        appendToList(new JavaInstanceOfInstr(result, object, klass));
    }

    public void emitLcmp(AllocatableValue result, AllocatableValue left, AllocatableValue right) {
        appendToList(new Op2Instr(Mnemonic.LCMP, result, left, right));
    }

    public void emitFcmp(AllocatableValue result, AllocatableValue left, AllocatableValue right, boolean isUnorderedLess) {
        appendToList(new Op2Instr(isUnorderedLess ? Mnemonic.FCMPU : Mnemonic.FCMP, result, left, right));
    }

    public void emitAllocateArray(NewArrayStub stub, AllocatableValue klassReg, AllocatableValue dest, AllocatableValue len, AllocatableValue temp1, AllocatableValue temp2, AllocatableValue temp3, AllocatableValue temp4, JavaKind elementType) {
        appendToList(new AllocateArrayInstr(stub, klassReg, dest, len, temp1, temp2, temp3, temp4, elementType));
    }

    public void emitLabel(LabelInstr labelInstr) {
        appendToList(labelInstr);
    }

    public void emitNeg(AllocatableValue from, AllocatableValue to) {
        appendToList(new Op2Instr(Mnemonic.NEG, to, from, AllocatableValue.ILLEGAL));
    }

    public void emitAnd(AllocatableValue dest, AllocatableValue left, AllocatableValue right) {
        appendToList(new Op2Instr(Mnemonic.AND, dest, left, right));
    }

    public void emitOr(AllocatableValue dest, AllocatableValue left, AllocatableValue right) {
        appendToList(new Op2Instr(Mnemonic.OR, dest, left, right));
    }

    public void emitXor(AllocatableValue dest, AllocatableValue left, AllocatableValue right) {
        appendToList(new Op2Instr(Mnemonic.XOR, dest, left, right));
    }

    public void emitShl(AllocatableValue dest, AllocatableValue value, AllocatableValue count) {
        appendToList(new Op2Instr(Mnemonic.SHL, dest, value, count));
    }

    public void emitShr(AllocatableValue dest, AllocatableValue value, AllocatableValue count) {
        appendToList(new Op2Instr(Mnemonic.SHR, dest, value, count));
    }

    public void emitUshr(AllocatableValue dest, AllocatableValue value, AllocatableValue count) {
        appendToList(new Op2Instr(Mnemonic.USHR, dest, value, count));
    }

    public void emitJavaCast(AllocatableValue result, AllocatableValue operand, int bytecode) {
        appendToList(new JavaTypeCastInstr(result, operand, bytecode));
    }

    public void emitAdd(AllocatableValue result, AllocatableValue left, AllocatableValue right) {
        appendToList(new Op2Instr(Mnemonic.ADD, result, left, right));
    }

    public void emitSub(AllocatableValue result, AllocatableValue left, AllocatableValue right) {
        appendToList(new Op2Instr(Mnemonic.SUB, result, left, right));
    }

    public void emitMul(AllocatableValue result, AllocatableValue left, AllocatableValue right) {
        appendToList(new Op2Instr(Mnemonic.MUL, result, left, right));
    }

    public void emitDiv(AllocatableValue result, AllocatableValue left, AllocatableValue right) {
        appendToList(new Op2Instr(Mnemonic.DIV, result, left, right));
    }

    public void emitRem(AllocatableValue result, AllocatableValue left, AllocatableValue right) {
        appendToList(new Op2Instr(Mnemonic.REM, result, left, right));
    }

    public void emitMov(AllocatableValue dest, AllocatableValue src) {
        appendToList(new Op1Instr(Mnemonic.MOV, dest, src));
    }

    public void emitCallRt(AllocatableValue result, Address rountine, AllocatableValue[] argument) {
        appendToList(new CallRtInstr(result, rountine, argument));
    }

    public void emitJavaCall(Mnemonic mnemonic, AllocatableValue result, JavaMethod method, AllocatableValue receiver, AllocatableValue[] arguments) {
        appendToList(new JavaCallInstr(mnemonic, result, method, receiver, arguments));
    }

    public void emitMembar(Mnemonic mnemonic) {
        appendToList(new Op0Instr(mnemonic, AllocatableValue.ILLEGAL));
    }

    public void emitJmp(BlockStartInstr block) {
        appendToList(new BranchInstr(Cond.Always, block));
    }

    public void emitJmp(RuntimeStub stub) {
        appendToList(new BranchInstr(Cond.Always, stub));
    }

    public void emitBranch(Cond condition, JavaKind type, BlockStartInstr block) {
        appendToList(new BranchInstr(condition, type, block));
    }

    public void emitReturn(AllocatableValue ret) {
        appendToList(new Op1Instr(Mnemonic.RETURN, AllocatableValue.ILLEGAL, ret));
    }


    public void emitNormalEntry() {
        appendToList(new Op0Instr(Mnemonic.NORMAL_ENTRY, AllocatableValue.ILLEGAL));
    }

    public void emitOsrEntry() {
        appendToList(new Op0Instr(Mnemonic.OSR_ENTRY, AllocatableValue.ILLEGAL));
    }

    private void appendToList(LirInstr instr) {
        if (TraceLIRGeneration) {
            Logger.logf("{}", instr.toString());
        }
        lir.appendLirInstr(currentBlockId, instr);
    }
}
