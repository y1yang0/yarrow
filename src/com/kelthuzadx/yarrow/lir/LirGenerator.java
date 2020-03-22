package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.*;
import com.kelthuzadx.yarrow.lir.instr.Opcode;
import com.kelthuzadx.yarrow.lir.instr.Operand2Instr;
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
        if (instr.isResolvedOperand()) {
            return;
        }

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
        if (instr.isResolvedOperand()) {
            return;
        }

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
        HirInstr left = instr.getLeft();
        HirInstr right = instr.getRight();
        if (!left.isResolvedOperand()) {
            left.visit(this);
        }
        if (!right.isResolvedOperand()) {
            right.visit(this);
        }
        LirOperand lo = left.getOperand();
        LirOperand ro = right.getOperand();
        LirOperand result = LirOperandFactory.createVirtualRegister(left.type());
        instr.setOperand(result);

        switch (instr.getOpcode()) {
            case Bytecode.IADD:
            case Bytecode.LADD:
            case Bytecode.FADD:
            case Bytecode.DADD:
                lir.appendLirInstr(currentBlockStartId, new Operand2Instr(Opcode.ADD, result, lo, ro));
                break;
            case Bytecode.ISUB:
            case Bytecode.LSUB:
            case Bytecode.FSUB:
            case Bytecode.DSUB:
                lir.appendLirInstr(currentBlockStartId, new Operand2Instr(Opcode.SUB, result, lo, ro));
                break;
            case Bytecode.IMUL:
            case Bytecode.LMUL:
            case Bytecode.FMUL:
            case Bytecode.DMUL:
                lir.appendLirInstr(currentBlockStartId, new Operand2Instr(Opcode.MUL, result, lo, ro));
                break;
            case Bytecode.IDIV:
            case Bytecode.LDIV:
                CompilerErrors.bailOut();
            case Bytecode.FDIV:
            case Bytecode.DDIV:
                lir.appendLirInstr(currentBlockStartId, new Operand2Instr(Opcode.DIV, result, lo, ro));
                break;
            case Bytecode.IREM:
            case Bytecode.LREM:
            case Bytecode.FREM:
            case Bytecode.DREM:
                lir.appendLirInstr(currentBlockStartId, new Operand2Instr(Opcode.REM, result, lo, ro));
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
}
