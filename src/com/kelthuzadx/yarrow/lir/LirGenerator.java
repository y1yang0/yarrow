package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.*;
import com.kelthuzadx.yarrow.lir.instr.*;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.lir.operand.LirOperandFactory;
import com.kelthuzadx.yarrow.optimize.InstructionVisitor;
import com.kelthuzadx.yarrow.util.CompilerErrors;

/**
 * Stateful low level IR generator
 */
public class LirGenerator extends InstructionVisitor {
    private int currentBlockStartId;
    private Lir lir;

    public LirGenerator(Lir lir) {
        this.currentBlockStartId = -1;
        this.lir = lir;
    }


    @Override
    public void visitMemBarrierInstr(MemBarrierInstr instr) {

    }

    @Override
    public void visitInstanceOfInstr(InstanceOfInstr instr) {

    }

    @Override
    public void visitShiftInstr(ShiftInstr instr) {

    }

    @Override
    public void visitBlockEndInstr(BlockEndInstr instr) {
    }

    @Override
    public void visitParamInstr(ParamInstr instr) {
        instr.setOperand(LirOperandFactory.createVirtualRegister(instr.type()));
    }

    @Override
    public void visitLogicInstr(LogicInstr instr) {

    }

    @Override
    public void visitMultiNewArrayInstr(MultiNewArrayInstr instr) {

    }

    @Override
    public void visitStoreIndexInstr(StoreIndexInstr instr) {

    }

    @Override
    public void visitStoreFieldInstr(StoreFieldInstr instr) {

    }

    @Override
    public void visitPhiInstr(PhiInstr instr) {

    }

    @Override
    public void visitBlockStartInstr(BlockStartInstr instr) {
        currentBlockStartId = instr.id();
    }

    @Override
    public void visitLoadFieldInstr(LoadFieldInstr instr) {

    }

    @Override
    public void visitCompareInstr(CompareInstr instr) {

    }

    @Override
    public void visitNegateInstr(NegateInstr instr) {

    }

    @Override
    public void visitConstantInstr(ConstantInstr instr) {
        instr.setOperand(LirOperandFactory.createConstInt(instr));
    }

    @Override
    public void visitTableSwitchInstr(TableSwitchInstr instr) {

    }

    @Override
    public void visitCheckCastInstr(CheckCastInstr instr) {

    }

    @Override
    public void visitAccessFieldInstr(AccessFieldInstr instr) {

    }

    @Override
    public void visitStateInstr(StateInstr instr) {

    }

    @Override
    public void visitLoadIndexInstr(LoadIndexInstr instr) {

    }

    @Override
    public void visitArithmeticInstr(ArithmeticInstr instr) {
        LirOperand left = instr.getLeft().getOperand(this);
        LirOperand right = instr.getRight().getOperand(this);
        LirOperand result = LirOperandFactory.createVirtualRegister(instr.getLeft().type());
        instr.setOperand(result);
        if (left != result) {
            mov(result, left);
            left = result;
        }
        switch (instr.getOpcode()) {
            case Bytecode.IADD:
            case Bytecode.LADD:
            case Bytecode.FADD:
            case Bytecode.DADD:
                add(result, left, right);
                break;
            case Bytecode.ISUB:
            case Bytecode.LSUB:
            case Bytecode.FSUB:
            case Bytecode.DSUB:
                sub(result, left, right);
                break;
            case Bytecode.IMUL:
            case Bytecode.LMUL:
            case Bytecode.FMUL:
            case Bytecode.DMUL:
                mul(result, left, right);
                break;
            case Bytecode.IDIV:
            case Bytecode.LDIV:
                CompilerErrors.bailOut();
            case Bytecode.FDIV:
            case Bytecode.DDIV:
                div(result, left, right);
                break;
            case Bytecode.IREM:
            case Bytecode.LREM:
            case Bytecode.FREM:
            case Bytecode.DREM:
                rem(result, left, right);
                break;
            default:
                YarrowError.shouldNotReachHere();
        }
    }

    @Override
    public void visitArrayLenInstr(ArrayLenInstr instr) {

    }

    @Override
    public void visitMonitorExitInstr(MonitorExitInstr instr) {

    }

    @Override
    public void visitMonitorEnterInstr(MonitorEnterInstr instr) {

    }

    @Override
    public void visitLookupSwitchInstr(LookupSwitchInstr instr) {

    }

    @Override
    public void visitIfInstr(IfInstr instr) {

    }

    @Override
    public void visitNewInstr(NewInstr instr) {

    }

    @Override
    public void visitTypeCastInstr(TypeCastInstr instr) {
        LirOperand fromOperand = instr.getFrom().getOperand(this);
        LirOperand fromResult = LirOperandFactory.createVirtualRegister(instr.type());

        LirOperand toOperand = fromOperand;
        LirOperand toResult = fromResult;

        appendToList(new Op1TypeCastInstr(toResult, toOperand, instr.getOpcode()));

        if (fromResult != toResult) {
            mov(toResult, fromResult);
        }
        instr.setOperand(fromResult);
    }

    @Override
    public void visitInstruction(HirInstr instr) {

    }

    @Override
    public void visitAccessArrayInstr(AccessArrayInstr instr) {

    }

    @Override
    public void visitNewObjectArrayInstr(NewObjectArrayInstr instr) {

    }

    @Override
    public void visitCallInstr(CallInstr instr) {

    }

    @Override
    public void visitGotoInstr(GotoInstr instr) {

    }

    @Override
    public void visitReturnInstr(ReturnInstr instr) {

    }

    @Override
    public void visitOp2Instr(Op2Instr instr) {
        YarrowError.shouldNotReachHere();
    }

    @Override
    public void visitThrowInstr(ThrowInstr instr) {

    }

    @Override
    public void visitNewTypeArrayInstr(NewTypeArrayInstr instr) {

    }

    private void add(LirOperand result, LirOperand left, LirOperand right) {
        appendToList(new Operand2Instr(Opcode.ADD, result, left, right));
    }

    private void sub(LirOperand result, LirOperand left, LirOperand right) {
        appendToList(new Operand2Instr(Opcode.SUB, result, left, right));
    }

    private void mul(LirOperand result, LirOperand left, LirOperand right) {
        appendToList(new Operand2Instr(Opcode.MUL, result, left, right));
    }

    private void div(LirOperand result, LirOperand left, LirOperand right) {
        appendToList(new Operand2Instr(Opcode.DIV, result, left, right));
    }

    private void rem(LirOperand result, LirOperand left, LirOperand right) {
        appendToList(new Operand2Instr(Opcode.REM, result, left, right));
    }

    private void mov(LirOperand dest, LirOperand src) {
        appendToList(new Operand1Instr(Opcode.MOV, dest, src));
    }

    private void appendToList(LirInstr instr) {
        lir.appendLirInstr(currentBlockStartId, instr);
    }
}
