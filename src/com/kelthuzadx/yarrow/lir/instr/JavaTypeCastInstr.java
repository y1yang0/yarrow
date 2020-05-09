package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.AllocatableValue;

public class JavaTypeCastInstr extends Op1Instr {
    private final int bytecode;

    public JavaTypeCastInstr(AllocatableValue result, AllocatableValue operand, int bytecode) {
        super(Mnemonic.TYPE_CAST, result, operand);
        this.bytecode = bytecode;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: cast {},{}", super.id, stringify(result), stringify(operand));
    }
}
