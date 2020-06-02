package com.kelthuzadx.yarrow.lir.regalloc;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.lir.instr.*;
import com.kelthuzadx.yarrow.lir.operand.XRegister;
import com.kelthuzadx.yarrow.optimize.LirInstrVisitor;

import java.util.ArrayList;

public class InstrStateVisitor extends LirInstrVisitor {
    private ArrayList<XRegister> input;
    private ArrayList<XRegister> output;
    private ArrayList<XRegister> temp;

    private boolean hasCall;


    public InstrStateVisitor() {
        input = new ArrayList<>();
        output = new ArrayList<>();
        temp = new ArrayList<>();
    }

    public boolean hasCall() {
        return hasCall;
    }

    public void reset() {
        input = new ArrayList<>();
        output = new ArrayList<>();
        temp = new ArrayList<>();
    }

    public ArrayList<XRegister> getInput() {
        return input;
    }

    public ArrayList<XRegister> getOutput() {
        return output;
    }

    public ArrayList<XRegister> getTemp() {
        return temp;
    }

    @Override
    public void visitAllocateArrayInstr(AllocateArrayInstr instr) {
        if (instr.getKlassReg() instanceof XRegister) {
            input.add((XRegister) instr.getKlassReg());
        }

        if (instr.getLength() instanceof XRegister) {
            input.add((XRegister) instr.getLength());
        }

        if (instr.getTemp1() instanceof XRegister) {
            temp.add((XRegister) instr.getTemp1());
        }
        if (instr.getTemp2() instanceof XRegister) {
            temp.add((XRegister) instr.getTemp2());
        }

        if (instr.getTemp3() instanceof XRegister) {
            temp.add((XRegister) instr.getTemp3());
        }

        if (instr.getTemp4() instanceof XRegister) {
            temp.add((XRegister) instr.getTemp4());
        }


        if (instr.operandResult() instanceof XRegister) {
            output.add((XRegister) instr.operandResult());
        }
    }

    @Override
    public void visitBranchInstr(BranchInstr instr) {

    }

    @Override
    public void visitCallRtInstr(CallRtInstr instr) {
        for (var value : instr.getArgument()) {
            if (value instanceof XRegister) {
                input.add((XRegister) value);
            }
        }

        if (instr.operandResult() instanceof XRegister) {
            output.add((XRegister) instr.operandResult());
        }
        hasCall = true;
    }

    @Override
    public void visitJavaCallInstr(JavaCallInstr instr) {
        if (instr.getReceiver() instanceof XRegister) {
            input.add((XRegister) instr.getReceiver());
        }

        for (var value : instr.getArguments()) {
            if (value instanceof XRegister) {
                input.add((XRegister) value);
            }
        }

        if (instr.operandResult() instanceof XRegister) {
            output.add((XRegister) instr.operandResult());
        }
        hasCall = true;
    }

    @Override
    public void visitJavaCheckCastInstr(JavaCheckCastInstr instr) {
        if (instr.getObject() instanceof XRegister) {
            input.add((XRegister) instr.getObject());
        }

        if (instr.operandResult() instanceof XRegister) {
            output.add((XRegister) instr.operandResult());
        }
    }

    @Override
    public void visitJavaInstanceOfInstr(JavaInstanceOfInstr instr) {

    }

    @Override
    public void visitJavaTypeCastInstr(JavaTypeCastInstr instr) {
        if (instr.operand1() instanceof XRegister) {
            input.add((XRegister) instr.operand1());
        }
        if (instr.operandResult() instanceof XRegister) {
            output.add((XRegister) instr.operandResult());
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
                if (instr.operandResult() instanceof XRegister) {
                    output.add((XRegister) instr.operandResult());
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
                if (instr.operand1() instanceof XRegister) {
                    input.add((XRegister) instr.operand1());
                }
                if (instr.operandResult() instanceof XRegister) {
                    output.add((XRegister) instr.operandResult());
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
                if (instr.operand1() instanceof XRegister) {
                    input.add((XRegister) instr.operand1());
                }
                if (instr.operand2() instanceof XRegister) {
                    input.add((XRegister) instr.operand2());
                }
                if (instr.operandResult() instanceof XRegister) {
                    output.add((XRegister) instr.operandResult());
                }
                break;
            default:
                break;
        }
    }
}
