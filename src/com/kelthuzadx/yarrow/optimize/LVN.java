package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.hir.HIR;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.hir.instr.Instruction;
import com.kelthuzadx.yarrow.phase.Phase;
import com.kelthuzadx.yarrow.util.Logger;

import java.util.*;

public class LVN{
    private HashMap<Integer,Instruction> valueMap;

    public LVN(){
        valueMap = new HashMap<>();
    }

    public Instruction findReplacement(Instruction instr){
        var i = valueMap.get(instr);
        if(i!=null){
            return i;
        }
        valueMap.put(instr.hashCode(),instr);
        return instr;
    }

}
