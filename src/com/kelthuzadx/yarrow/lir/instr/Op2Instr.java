package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import jdk.vm.ci.meta.JavaKind;

public class Op2Instr extends LirInstr {
    private JavaKind type;
    private LirOperand loprerand;
    private LirOperand roprerand;
}
