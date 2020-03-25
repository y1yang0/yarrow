package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

import java.util.List;

public class ThrowInstr extends BlockEndInstr {
    private HirInstr exception;

    public ThrowInstr(VmState stateBefore, List<BlockStartInstr> successor, HirInstr exception) {
        super(JavaKind.Illegal, stateBefore, successor);
        this.exception = exception;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: throw i{}", super.id, exception.id);
    }
}
