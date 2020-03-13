package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;

import java.util.List;

public class BlockEndInstr extends StateInstr {
    private BlockStartInstr start;
    private List<BlockStartInstr> successor;

    public BlockEndInstr(Value value, VmState stateBefore, List<BlockStartInstr> successor) {
        super(value, stateBefore);
        this.start = null;
        this.successor = successor;
    }

    public List<BlockStartInstr> getSuccessor() {
        return successor;
    }

    public void setBlockStart(BlockStartInstr start) {
        this.start = start;
    }
}
