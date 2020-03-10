package com.kelthuzadx.yarrow.hir.instr;

import java.util.List;

public class GotoInstr extends BlockEndInstr {
    public GotoInstr(List<BlockStartInstr> successor) {
        super(successor);
    }
}
