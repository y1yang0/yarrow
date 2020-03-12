package com.kelthuzadx.yarrow.util;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.hir.instr.Instruction;
import jdk.vm.ci.meta.JavaKind;

import java.util.Stack;

public class Constrain {

    public static void matchInt(int a, int b){
        if(a!=b){
            throw new YarrowError("assertion failure");
        }
    }

    public static void matchVmState(VmState a, VmState b){
        if(a.getStackSize()!=b.getStackSize() ||
                a.getLocalSize()!=b.getLocalSize() ||
                a.getLockSize()!=b.getLockSize()){
            throw new YarrowError("two VmState should be identical");
        }
        Stack<Instruction> as = a.getStack();
        Stack<Instruction> bs = b.getStack();
        for(int i=0;i<as.size();i++){
            if(bs.get(i).isType(as.get(i).getType())){
                throw new YarrowError("two VmState should be identical");
            }
        }
        // TODO: should check lock either
    }

}
