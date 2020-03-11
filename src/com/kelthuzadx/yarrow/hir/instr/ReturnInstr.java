package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

import java.util.ArrayList;
import java.util.List;

public class ReturnInstr extends BlockEndInstr {
    private Instruction returnValue;

    public ReturnInstr(Instruction returnValue) {
        super(new Value(returnValue==null?JavaKind.Void:returnValue.getType()),new ArrayList<>());
        this.returnValue = returnValue;
    }
}
