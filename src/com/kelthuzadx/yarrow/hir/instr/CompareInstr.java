package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

public class CompareInstr extends Op2Instr {
    public CompareInstr(int opcode, Instruction left, Instruction right) {
        super(new Value(JavaKind.Int),opcode, left, right);
    }
}
