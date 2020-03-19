package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.BlockEndInstr;
import com.kelthuzadx.yarrow.hir.instr.Instruction;

import java.util.HashSet;
import java.util.Set;

public class LVN {
    private Set<Instruction> valueSet;
    private Instruction replacement;

    public LVN() {
        valueSet = new HashSet<>();
    }

    public boolean hasReplacement(Instruction instr) {
        for (Instruction i : valueSet) {
            if (i.hashCode() == instr.hashCode() && i.equals(instr)) {
                YarrowError.guarantee(!(instr instanceof BlockEndInstr),"should never value numbering BlockEndInstr and its subclasses");
                replacement = i;
                return true;
            }
        }
        valueSet.add(instr);
        return false;
    }

    public Instruction getReplacement() {
        return replacement;
    }
}
