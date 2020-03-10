package com.kelthuzadx.yarrow.hir.instr;

import java.util.List;

public class BlockEndInstr extends Instruction{
    private List<BlockStartInstr> successor;

    public BlockEndInstr(List<BlockStartInstr> successor) {
        this.successor = successor;
    }
}
