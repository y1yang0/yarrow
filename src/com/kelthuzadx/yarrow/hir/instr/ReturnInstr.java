package com.kelthuzadx.yarrow.hir.instr;

import java.util.List;

public class ReturnInstr extends BlockEndInstr {
    private Instruction returnValue;

    public ReturnInstr(Instruction returnValue) {
        super(null/*no successor*/);
        this.returnValue = returnValue;
    }
}
