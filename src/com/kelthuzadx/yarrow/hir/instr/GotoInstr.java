package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

import java.util.List;

public class GotoInstr extends BlockEndInstr {
    public GotoInstr(List<BlockStartInstr> successor) {
        super(new Value(JavaKind.Illegal),successor);
    }
}
