package com.kelthuzadx.yarrow.lir.regalloc;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.lir.instr.*;
import com.kelthuzadx.yarrow.lir.operand.VirtualRegister;
import com.kelthuzadx.yarrow.optimize.LirInstrVisitor;
import com.kelthuzadx.yarrow.util.Logger;

import java.util.ArrayList;

public class InstrStateVisitor extends LirInstrVisitor {
    private ArrayList<VirtualRegister> input;
    private ArrayList<VirtualRegister> output;
    private ArrayList<VirtualRegister> temp;

    public InstrStateVisitor() {
    }

    public void reset() {
        input = new ArrayList<>();
        output = new ArrayList<>();
        temp = new ArrayList<>();
    }

    public ArrayList<VirtualRegister> getInput() {
        return input;
    }

    public ArrayList<VirtualRegister> getOutput() {
        return output;
    }

    public ArrayList<VirtualRegister> getTemp() {
        return temp;
    }

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
        switch (instr.getMnemonic()){
            case BRANCH:
            case LABEL:
                // Nothing need to do
                break;
            case MEMBAR:
            case MEMBAR_LOAD_LOAD:
            case MEMBAR_LOAD_STORE:
            case MEMBAR_STORE_LOAD:
            case MEMBAR_STORE_STORE:
            case MEMBAR_ACQUIRE:
            case MEMBAR_RELEASE:
            case OSR_ENTRY:
            case NORMAL_ENTRY:
                if(instr.operandResult() instanceof VirtualRegister){
                    output.add((VirtualRegister)instr.operandResult());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void visitOp1Instr(Op1Instr instr) {
        switch (instr.getMnemonic()){
            case TYPE_CAST:
                if(instr.operand1() instanceof VirtualRegister){
                    input.add((VirtualRegister) instr.operand1());
                }
                if(instr.operandResult() instanceof VirtualRegister){
                    output.add((VirtualRegister)instr.operandResult());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void visitOp2Instr(Op2Instr instr) {

    }
}
