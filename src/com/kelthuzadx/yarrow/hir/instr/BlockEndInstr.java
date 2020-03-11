package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;

import java.util.List;

public class BlockEndInstr extends Instruction{
    private List<BlockStartInstr> successor;

    public BlockEndInstr(Value value,List<BlockStartInstr> successor) {
        super(value);
        this.successor = successor;
    }
}
