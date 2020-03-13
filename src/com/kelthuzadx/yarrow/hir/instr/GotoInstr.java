package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

import java.util.ArrayList;

public class GotoInstr extends BlockEndInstr {
    public GotoInstr(VmState stateBefore, BlockStartInstr successor) {
        super(new Value(JavaKind.Illegal), stateBefore, new ArrayList<>() {{
            add(successor);
        }});
    }

    @Override
    public String toString() {
        return Logger.format("i{}: goto i{}", super.id, super.getSuccessor().get(0).id);
    }
}
