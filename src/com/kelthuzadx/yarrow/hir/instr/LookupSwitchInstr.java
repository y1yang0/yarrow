package com.kelthuzadx.yarrow.hir.instr;

import java.util.List;

public class LookupSwitchInstr extends BlockEndInstr {
    private Instruction index;
    private int[] key;

    public LookupSwitchInstr(List<BlockStartInstr> successor, Instruction index, int[] key) {
        super(successor);
        this.index = index;
        this.key = key;
    }￿
}