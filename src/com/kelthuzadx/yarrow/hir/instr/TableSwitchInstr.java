package com.kelthuzadx.yarrow.hir.instr;

import java.util.List;

public class TableSwitchInstr extends BlockEndInstr {
    private Instruction index;
    private int lowKey;
    private int highKey;

    public TableSwitchInstr(List<BlockStartInstr> successor, Instruction index, int lowKey, int highKey) {
        super(successor);
        this.index = index;
        this.lowKey = lowKey;
        this.highKey = highKey;
    }
}
