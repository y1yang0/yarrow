package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class CompareInstr extends Op2Instr {
    public CompareInstr(int opcode, Instruction left, Instruction right) {
        super(new Value(JavaKind.Int), opcode, left, right);
    }

    @Override
    public String toString() {
        return Logger.format("i{}: i{} == i{}", super.id, super.left.id, super.right.id);
    }
}
