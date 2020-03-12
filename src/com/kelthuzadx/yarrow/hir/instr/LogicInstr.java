package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class LogicInstr extends Op2Instr {
    public LogicInstr(int opcode, Instruction left, Instruction right) {
        super(new Value(left.getType()),opcode, left, right);
        if(!right.isType(left.getType())){
            throw new YarrowError("Incompatible operand type");
        }
    }


    @Override
    public String toString() {
        String op= "";
        switch (super.opcode){
            case Bytecode.IAND:
            case Bytecode.LAND:op="&";break;
            case Bytecode.IOR:
            case Bytecode.LOR:op="|";break;
            case Bytecode.IXOR:
            case Bytecode.LXOR:op="^";break;
        }
        return Logger.f("i{}: i{} {} i{}",super.id,left.id,op,right.id);
    }
}
