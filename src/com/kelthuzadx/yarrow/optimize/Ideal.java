package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.hir.HIR;
import com.kelthuzadx.yarrow.hir.InstructionVisitor;
import com.kelthuzadx.yarrow.hir.instr.*;
import com.kelthuzadx.yarrow.phase.Phase;
import jdk.vm.ci.common.JVMCIError;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * Ideal iterates bytecodes from each basic block and applies many local optimizations
 * on single instruction. Many classic optimization technique such as constant folding,
 * dead code will be combined together.
 *
 * @author kelthuzadx
 */
public class Ideal extends InstructionVisitor implements Phase {
    private HIR hir;

    public Ideal(HIR hir) {
        this.hir = hir;
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
        JVMCIError.shouldNotReachHere();
    }

    @Override
    public void visitLoadIndexInstr(LoadIndexInstr instr) {

    }

    @Override
    public void visitArithmeticInstr(ArithmeticInstr instr) {

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
    public void visitInstruction(Instruction instr) {
        JVMCIError.shouldNotReachHere();
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
        JVMCIError.shouldNotReachHere();
    }

    @Override
    public void visitThrowInstr(ThrowInstr instr) {

    }

    @Override
    public void visitNewTypeArrayInstr(NewTypeArrayInstr instr) {

    }

    @Override
    public Phase build() {
        Queue<BlockStartInstr> workList = new ArrayDeque<>();
        Set<Integer> visit = new HashSet<>();


        BlockStartInstr start = hir.getEntryBlock();
        workList.add(start);
        while (!workList.isEmpty()) {
            var block = workList.remove();
            if (!visit.contains(block.getInstrId())) {
                visit.add(block.getInstrId());
                {
                    BlockEndInstr end = block.getBlockEnd();
                    Instruction cur = block;
                    while (cur != end) {
                        cur.visit(this);
                        cur = cur.getNext();
                    }
                    cur.visit(this);
                }
                workList.addAll(block.getBlockEnd().getSuccessor());
            }
        }
        return this;
    }

    @Override
    public String name() {
        return "Ideal Optimization";
    }

    @Override
    public void log() {

    }
}
