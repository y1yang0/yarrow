package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GotoInstr extends BlockEndInstr {
    public GotoInstr(List<BlockStartInstr> successor) {
        super(new Value(JavaKind.Illegal),successor);
    }

    @Override
    public String toString() {
        return Logger.f("i{}: goto {}",super.id,
                super.getSuccessor().stream().map(succ-> "#"+succ.getBlockId()+" ").collect(Collectors.toList())
        );
    }
}
