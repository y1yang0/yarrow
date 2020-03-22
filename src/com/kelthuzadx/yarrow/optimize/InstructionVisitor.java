package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.hir.instr.*;

public abstract class InstructionVisitor {

    public abstract void visitMemBarrierInstr(MemBarrierInstr instr);

    public abstract void visitInstanceOfInstr(InstanceOfInstr instr);

    public abstract void visitShiftInstr(ShiftInstr instr);

    public abstract void visitBlockEndInstr(BlockEndInstr instr);

    public abstract void visitParamInstr(ParamInstr instr);

    public abstract void visitLogicInstr(LogicInstr instr);

    public abstract void visitMultiNewArrayInstr(MultiNewArrayInstr instr);

    public abstract void visitStoreIndexInstr(StoreIndexInstr instr);

    public abstract void visitStoreFieldInstr(StoreFieldInstr instr);

    public abstract void visitPhiInstr(PhiInstr instr);

    public abstract void visitBlockStartInstr(BlockStartInstr instr);

    public abstract void visitLoadFieldInstr(LoadFieldInstr instr);

    public abstract void visitCompareInstr(CompareInstr instr);

    public abstract void visitNegateInstr(NegateInstr instr);

    public abstract void visitConstantInstr(ConstantInstr instr);

    public abstract void visitTableSwitchInstr(TableSwitchInstr instr);

    public abstract void visitCheckCastInstr(CheckCastInstr instr);

    public abstract void visitAccessFieldInstr(AccessFieldInstr instr);

    public abstract void visitStateInstr(StateInstr instr);

    public abstract void visitLoadIndexInstr(LoadIndexInstr instr);

    public abstract void visitArithmeticInstr(ArithmeticInstr instr);

    public abstract void visitArrayLenInstr(ArrayLenInstr instr);

    public abstract void visitMonitorExitInstr(MonitorExitInstr instr);

    public abstract void visitMonitorEnterInstr(MonitorEnterInstr instr);

    public abstract void visitLookupSwitchInstr(LookupSwitchInstr instr);

    public abstract void visitIfInstr(IfInstr instr);

    public abstract void visitNewInstr(NewInstr instr);

    public abstract void visitTypeCastInstr(TypeCastInstr instr);

    public abstract void visitInstruction(HirInstr instr);

    public abstract void visitAccessArrayInstr(AccessArrayInstr instr);

    public abstract void visitNewObjectArrayInstr(NewObjectArrayInstr instr);

    public abstract void visitCallInstr(CallInstr instr);

    public abstract void visitGotoInstr(GotoInstr instr);

    public abstract void visitReturnInstr(ReturnInstr instr);

    public abstract void visitOp2Instr(Op2Instr instr);

    public abstract void visitThrowInstr(ThrowInstr instr);

    public abstract void visitNewTypeArrayInstr(NewTypeArrayInstr instr);
}
