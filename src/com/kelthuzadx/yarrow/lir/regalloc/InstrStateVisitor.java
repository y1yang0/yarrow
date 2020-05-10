package com.kelthuzadx.yarrow.lir.regalloc;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.*;
import com.kelthuzadx.yarrow.lir.instr.*;
import com.kelthuzadx.yarrow.optimize.HirInstrVisitor;
import com.kelthuzadx.yarrow.optimize.LirInstrVisitor;
import jdk.vm.ci.meta.AllocatableValue;

public class InstrStateVisitor extends LirInstrVisitor {
    private AllocatableValue[] input;
    private AllocatableValue[] output;
    private AllocatableValue[] temp;

    public InstrStateVisitor(){}


    @Override
    public void visitAllocateArrayInstr(AllocateArrayInstr instr) {

    }

    @Override
    public void visitBranchInstr(BranchInstr instr) {

    }

    @Override
    public void visitCallRtInstr(CallRtInstr instr) {

    }

    @Override
    public void visitJavaCallInstr(JavaCallInstr instr) {

    }

    @Override
    public void visitJavaCheckCastInstr(JavaCheckCastInstr instr) {

    }

    @Override
    public void visitJavaInstanceOfInstr(JavaInstanceOfInstr instr) {

    }

    @Override
    public void visitJavaTypeCastInstr(JavaTypeCastInstr instr) {

    }

    @Override
    public void visitLabelInstr(LabelInstr instr) {
    }

    @Override
    public void visitLirInstr(LirInstr instr) {
        YarrowError.shouldNotReachHere();
    }

    @Override
    public void visitOp0Instr(Op0Instr instr) {

    }

    @Override
    public void visitOp1Instr(Op1Instr instr) {

    }

    @Override
    public void visitOp2Instr(Op2Instr instr) {

    }
}
