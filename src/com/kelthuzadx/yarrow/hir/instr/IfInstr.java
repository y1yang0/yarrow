package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.value.Cond;

import java.util.List;

public class IfInstr extends BlockEndInstr {
    private Instruction left;
    private Instruction right;
    private Cond cond;

    public IfInstr(List<BlockStartInstr> successor, Instruction left, Instruction right, Cond cond) {
        super(successor);
        this.left = left;
        this.right = right;
        this.cond = cond;
    }
}
