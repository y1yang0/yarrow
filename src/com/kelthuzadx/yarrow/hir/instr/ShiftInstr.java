package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.CompilerErrors;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class ShiftInstr extends Op2Instr{
    public ShiftInstr(int opcode, Instruction left, Instruction right) {
        super(new Value(left.getType()),opcode, left, right);
    }

    @Override
    public String toString() {
        String op= "";
        switch (super.opcode){
            case Bytecode.ISHL:
            case Bytecode.LSHL:op="<<";break;
            case Bytecode.ISHR:
            case Bytecode.LSHR:op=">>";break;
            case Bytecode.IUSHR:
            case Bytecode.LUSHR: op=">>>";break;
            default:
                CompilerErrors.shouldNotReachHere();
        }
        return Logger.f("i{}: i{} {} i{}",super.id,left.id,op,right);
    }
}
