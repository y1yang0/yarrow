package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

import java.util.Arrays;
import java.util.List;

public class LookupSwitchInstr extends BlockEndInstr {
    private Instruction index;
    private int[] key;

    public LookupSwitchInstr(List<BlockStartInstr> successor, Instruction index, int[] key) {
        super(new Value(JavaKind.Illegal),successor);
        this.index = index;
        this.key = key;
    }

    @Override
    public String toString() {
        //FIXME: MORE USEFUL INFO
        return "LookupSwitchInstr";
    }
}