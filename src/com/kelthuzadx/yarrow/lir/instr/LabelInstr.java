package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.AllocatableValue;

public class LabelInstr extends Op0Instr {
    public LabelInstr() {
        super(Mnemonic.LABEL, AllocatableValue.ILLEGAL);
    }

    @Override
    public String toString() {
        return Logger.format("L{}:", super.id);
    }
}
