package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.lir.instr.*;

public abstract class LirInstrVisitor {
    public abstract void visitAllocateArrayInstr(AllocateArrayInstr instr);
    public abstract void visitBranchInstr(BranchInstr instr);
    public abstract void visitCallRtInstr(CallRtInstr instr);
    public abstract void visitJavaCallInstr(JavaCallInstr instr);
    public abstract void visitJavaCheckCastInstr(JavaCheckCastInstr instr);
    public abstract void visitJavaInstanceOfInstr(JavaInstanceOfInstr instr);
    public abstract void visitJavaTypeCastInstr(JavaTypeCastInstr instr);
    public abstract void visitLabelInstr(LabelInstr instr);
    public abstract void visitLirInstr(LirInstr instr);
    public abstract void visitOp0Instr(Op0Instr instr);
    public abstract void visitOp1Instr(Op1Instr instr);
    public abstract void visitOp2Instr(Op2Instr instr);
}
