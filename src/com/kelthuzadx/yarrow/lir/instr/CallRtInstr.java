package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.Address;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class CallRtInstr extends LirInstr {
    private Address routine;
    private LirOperand[] argument;

    public CallRtInstr(LirOperand result, Address routine, LirOperand[] argument) {
        super(Mnemonic.CallRt, result);
        this.routine = routine;
        this.argument = argument;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: call_rt {}", super.id, routine.toString());
    }
}
