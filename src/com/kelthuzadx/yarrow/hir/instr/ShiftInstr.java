package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.common.JVMCIError;

public class ShiftInstr extends Op2Instr {
    public ShiftInstr(int opcode, Instruction left, Instruction right) {
        super(new Value(left.getType()), opcode, left, right);
    }

    @Override
    public String toString() {
        String op = "";
        switch (super.opcode) {
            case Bytecode.ISHL:
            case Bytecode.LSHL:
                op = "<<";
                break;
            case Bytecode.ISHR:
            case Bytecode.LSHR:
                op = ">>";
                break;
            case Bytecode.IUSHR:
            case Bytecode.LUSHR:
                op = ">>>";
                break;
            default:
                JVMCIError.shouldNotReachHere();
        }
        return Logger.format("i{}: i{} {} i{}", super.id, left.id, op, right.id);
    }
}
