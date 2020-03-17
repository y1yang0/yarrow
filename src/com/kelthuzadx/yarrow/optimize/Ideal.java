package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.HIR;
import com.kelthuzadx.yarrow.hir.InstructionVisitor;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.instr.*;
import com.kelthuzadx.yarrow.phase.Phase;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.common.JVMCIError;
import jdk.vm.ci.meta.JavaKind;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * Each time HirBuilder appends new SSA instruction into basic block, Ideal would apply
 * applies many local optimizations on this newly created single instruction, it may or
 * may not transform newly created instruction. Many classic optimization technique
 * such as constant folding, dead code will be combined together, so I called it "Ideal".
 *
 * @author kelthuzadx
 */
public class Ideal extends InstructionVisitor {
    private static Ideal instance = new Ideal();

    private Instruction ideal;

    private Ideal(){}

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
        Instruction leftInstr = instr.getLeft();
        Instruction rightInstr = instr.getRight();
        if (leftInstr instanceof ConstantInstr && rightInstr instanceof ConstantInstr) {
            // i1: 1+2 -> i2: 3
            switch (leftInstr.type()) {
                case Int: {
                    int left = leftInstr.value();
                    int right = rightInstr.value();
                    ConstantInstr newInstr = new ConstantInstr(new Value(JavaKind.Int, left+right));
                    break;
                }
                default:
                    break;
            }
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
    public void visitInstruction(Instruction instr) {
        YarrowError.shouldNotReachHere();
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
        YarrowError.unimplemented();
    }

    @Override
    public void visitNewTypeArrayInstr(NewTypeArrayInstr instr) {

    }

    private void logIdeal(Instruction before){
        Logger.logf("======{} -> {} idealized=====",before.toString(),ideal.toString());
    }

    public static Instruction optimize(Instruction instr) {
        // Set ideal to unoptimized instruction
        instance.ideal = instr;
        // Try to apply local optimizations
        instr.visit(instance);
        if(instr != instance.ideal){
            instance.logIdeal(instr);
        }
        // Return idealized instruction
        return instance.ideal;
    }
}
