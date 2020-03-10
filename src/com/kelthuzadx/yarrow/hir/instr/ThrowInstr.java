package com.kelthuzadx.yarrow.hir.instr;

import java.util.List;

public class ThrowInstr extends BlockEndInstr {
    private Instruction exception;

    public ThrowInstr(List<BlockStartInstr> successor,Instruction exception) {
        super(successor);
        this.exception = exception;
    }
}
