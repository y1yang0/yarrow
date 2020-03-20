package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.BlockEndInstr;
import com.kelthuzadx.yarrow.hir.instr.Instruction;
import com.kelthuzadx.yarrow.hir.instr.LoadFieldInstr;
import com.kelthuzadx.yarrow.hir.instr.StoreFieldInstr;

import java.util.HashSet;
import java.util.Set;

public class LVN {
    private Set<Instruction> valueSet;
    private Instruction replacement;

    public LVN() {
        valueSet = new HashSet<>();
    }


    private void killValue(Instruction instr){
        if(instr instanceof StoreFieldInstr){
            var temp = (StoreFieldInstr)instr;
            for(Instruction i:valueSet){
                if(i instanceof LoadFieldInstr){
                    var existing = (LoadFieldInstr)i;
                    if(existing.getOffset() == temp.getOffset() &&
                    existing.getObject() == temp.getObject() &&
                    existing.getField() == temp.getField()){
                        //kill
                    }
                }
            }
        }
    }

    public boolean hasReplacement(Instruction instr) {
        for (Instruction i : valueSet) {
            if (i.hashCode() == instr.hashCode() && i.equals(instr)) {
                YarrowError.guarantee(!(instr instanceof BlockEndInstr), "should never value numbering BlockEndInstr and its subclasses");
                replacement = i;
                // assignment statement of monitor statement might kill instructions in valueSet.
                killValue(instr);
                return true;
            }
        }
        valueSet.add(instr);
        killValue(instr);
        return false;
    }

    public Instruction getReplacement() {
        return replacement;
    }
}
