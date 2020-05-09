package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.util.Increment;
import jdk.vm.ci.code.StackSlot;
import jdk.vm.ci.meta.AllocatableValue;

public class LirInstr {
    protected int id;
    protected Mnemonic mnemonic;
    protected AllocatableValue result;

    public LirInstr(Mnemonic mnemonic, AllocatableValue result) {
        this.id = Increment.next(LirInstr.class);
        this.mnemonic = mnemonic;
        this.result = result;
    }

    public static String stringify(AllocatableValue value) {
        if (value instanceof StackSlot) {
            return "[rbp+" + ((StackSlot) value).getRawOffset() + "]";
        }
        return value.toString();
    }
}
