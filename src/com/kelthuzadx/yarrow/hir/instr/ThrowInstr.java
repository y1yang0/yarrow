package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

import java.util.List;

public class ThrowInstr extends BlockEndInstr {
    private Instruction exception;

    public ThrowInstr(List<BlockStartInstr> successor,Instruction exception) {
        super(new Value(JavaKind.Illegal),successor);
        this.exception = exception;
    }
}
