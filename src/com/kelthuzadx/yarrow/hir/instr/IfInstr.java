package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

import java.util.List;

public class IfInstr extends BlockEndInstr {
    private Instruction left;
    private Instruction right;
    private Cond cond;

    public IfInstr(List<BlockStartInstr> successor, Instruction left, Instruction right, Cond cond) {
        super(new Value(JavaKind.Illegal),successor);
        this.left = left;
        this.right = right;
        this.cond = cond;
    }
}
