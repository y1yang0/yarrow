package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.VmState;
import jdk.vm.ci.meta.JavaKind;

import java.util.List;

public class BlockEndInstr extends StateInstr {
    private BlockStartInstr start;
    private final List<BlockStartInstr> successor;

    public BlockEndInstr(JavaKind type, VmState stateBefore, List<BlockStartInstr> successor) {
        super(type, stateBefore);
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
