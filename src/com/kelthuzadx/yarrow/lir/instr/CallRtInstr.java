package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.Address;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.AllocatableValue;

public class CallRtInstr extends LirInstr {
    private final Address routine;
    private final AllocatableValue[] argument;

    public CallRtInstr(AllocatableValue result, Address routine, AllocatableValue[] argument) {
        super(Mnemonic.CALL_RT, result);
        this.routine = routine;
        this.argument = argument;
    }

    public AllocatableValue[] getArgument() {
        return argument;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: call_rt {}", super.id, stringify(routine));
    }
}
