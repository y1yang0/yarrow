package com.kelthuzadx.yarrow.lir.regalloc;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.lir.instr.*;
import com.kelthuzadx.yarrow.lir.operand.VirtualRegister;
import com.kelthuzadx.yarrow.optimize.LirInstrVisitor;

import java.util.ArrayList;

public class InstrStateVisitor extends LirInstrVisitor {
    private ArrayList<VirtualRegister> input;
    private ArrayList<VirtualRegister> output;
    private ArrayList<VirtualRegister> temp;

    private boolean hasCall;


    public boolean hasCall() {
        return hasCall;
    }

    public InstrStateVisitor() {
        input = new ArrayList<>();
        output = new ArrayList<>();
        temp = new ArrayList<>();
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
        if (instr.getKlassReg() instanceof VirtualRegister) {
            input.add((VirtualRegister) instr.getKlassReg());
        }

        if (instr.getLength() instanceof VirtualRegister) {
            input.add((VirtualRegister) instr.getLength());
        }

        if (instr.getTemp1() instanceof VirtualRegister) {
            temp.add((VirtualRegister) instr.getTemp1());
        }
        if (instr.getTemp2() instanceof VirtualRegister) {
            temp.add((VirtualRegister) instr.getTemp2());
        }

        if (instr.getTemp3() instanceof VirtualRegister) {
            temp.add((VirtualRegister) instr.getTemp3());
        }

        if (instr.getTemp4() instanceof VirtualRegister) {
            temp.add((VirtualRegister) instr.getTemp4());
        }


        if (instr.operandResult() instanceof VirtualRegister) {
            output.add((VirtualRegister) instr.operandResult());
        }
    }

    @Override
    public void visitBranchInstr(BranchInstr instr) {

    }

    @Override
    public void visitCallRtInstr(CallRtInstr instr) {
        for(var value:instr.getArgument()){
            if(value instanceof VirtualRegister){
                input.add((VirtualRegister)value);
            }
        }

        if (instr.operandResult() instanceof VirtualRegister) {
            output.add((VirtualRegister) instr.operandResult());
        }
        hasCall = true;
    }

    @Override
    public void visitJavaCallInstr(JavaCallInstr instr) {
        if(instr.getReceiver() instanceof VirtualRegister){
            input.add((VirtualRegister)instr.getReceiver());
        }

        for(var value:instr.getArguments()){
            if(value instanceof VirtualRegister){
                input.add((VirtualRegister)value);
            }
        }

        if (instr.operandResult() instanceof VirtualRegister) {
            output.add((VirtualRegister) instr.operandResult());
        }
        hasCall = true;
    }

    @Override
    public void visitJavaCheckCastInstr(JavaCheckCastInstr instr) {
        if(instr.getObject() instanceof VirtualRegister){
            input.add((VirtualRegister) instr.getObject());
        }

        if (instr.operandResult() instanceof VirtualRegister) {
            output.add((VirtualRegister) instr.operandResult());
        }
    }

    @Override
    public void visitJavaInstanceOfInstr(JavaInstanceOfInstr instr) {

    }

    @Override
    public void visitJavaTypeCastInstr(JavaTypeCastInstr instr) {
        if (instr.operand1() instanceof VirtualRegister) {
            input.add((VirtualRegister) instr.operand1());
        }
        if (instr.operandResult() instanceof VirtualRegister) {
            output.add((VirtualRegister) instr.operandResult());
        }
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
        switch (instr.getMnemonic()) {
            case BRANCH:
            case LABEL:
            case OSR_ENTRY:
            case NORMAL_ENTRY:
                // Nothing need to do
                break;
            case MEMBAR:
            case MEMBAR_LOAD_LOAD:
            case MEMBAR_LOAD_STORE:
            case MEMBAR_STORE_LOAD:
            case MEMBAR_STORE_STORE:
            case MEMBAR_ACQUIRE:
            case MEMBAR_RELEASE:
                if (instr.operandResult() instanceof VirtualRegister) {
                    output.add((VirtualRegister) instr.operandResult());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void visitOp1Instr(Op1Instr instr) {
        switch (instr.getMnemonic()) {
            case MOV:
            case RETURN:
                if (instr.operand1() instanceof VirtualRegister) {
                    input.add((VirtualRegister) instr.operand1());
                }
                if (instr.operandResult() instanceof VirtualRegister) {
                    output.add((VirtualRegister) instr.operandResult());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void visitOp2Instr(Op2Instr instr) {
        switch (instr.getMnemonic()) {
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case REM:
            case SHL:
            case SHR:
            case USHR:
            case AND:
            case OR:
            case XOR:
            case NEG:
            case FCMP:
            case FCMPU:
            case LCMP:
            case CMP:
                if (instr.operand1() instanceof VirtualRegister) {
                    input.add((VirtualRegister) instr.operand1());
                }
                if (instr.operand2() instanceof VirtualRegister) {
                    input.add((VirtualRegister) instr.operand2());
                }
                if (instr.operandResult() instanceof VirtualRegister) {
                    output.add((VirtualRegister) instr.operandResult());
                }
                break;
            default:
                break;
        }
    }
}
