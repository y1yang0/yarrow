package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

import java.util.List;

public class ThrowInstr extends BlockEndInstr {
    private Instruction exception;

    public ThrowInstr(VmState stateBefore, List<BlockStartInstr> successor, Instruction exception) {
        super(new Value(JavaKind.Illegal), stateBefore, successor);
        this.exception = exception;
    }

    @Override
    public String toString() {
        return Logger.f("i{}: throw i{}", super.id, exception.id);
    }
}
