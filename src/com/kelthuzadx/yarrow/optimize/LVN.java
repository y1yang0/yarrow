package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.hir.HIR;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.hir.instr.Instruction;
import com.kelthuzadx.yarrow.phase.Phase;
import com.kelthuzadx.yarrow.util.Logger;

import java.util.*;

public class LVN{
    private Set<Instruction> valueSet;
    private Instruction replacement;

    public LVN(){
        valueSet = new HashSet<>();
    }

    public boolean hasReplacement(Instruction instr){
        for(Instruction i:valueSet){
            if(i.hashCode()== instr.hashCode() && i.equals(instr)){
                replacement = i;
                return true;
            }
        }
        valueSet.add(instr);
        return false;
    }

    public Instruction getReplacement(){
        return replacement;
    }
}
