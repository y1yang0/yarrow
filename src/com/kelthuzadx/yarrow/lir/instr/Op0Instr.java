package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class Op0Instr extends LirInstr {

    public Op0Instr(Mnemonic mnemonic, LirOperand result) {
        super(mnemonic, result);
    }

    @Override
    public String toString() {
        switch (mnemonic) {
            case MEMBAR:
                return Logger.format("i{}: membar full", super.id);
            case MEMBAR_LOAD_LOAD:
                return Logger.format("i{}: membar LoadLoad", super.id);
            case MEMBAR_LOAD_STORE:
                return Logger.format("i{}: membar LoadStore", super.id);
            case MEMBAR_STORE_LOAD:
                return Logger.format("i{}: membar StoreLoad", super.id);
            case MEMBAR_STORE_STORE:
                return Logger.format("i{}: membar StoreStore", super.id);
            case MEMBAR_ACQUIRE:
                return Logger.format("i{}: membar Acquire", super.id);
            case MEMBAR_RELEASE:
                return Logger.format("i{}: membar Release", super.id);
            case OSR_ENTRY:
                return Logger.format("i{}: osr_entry", super.id);
            case NORMAL_ENTRY:
                return Logger.format("i{}: normal_entry", super.id);
            default:
                break;
        }
        return Logger.format("i{}: {} {}", super.id, mnemonic.name().toLowerCase(), result.toString());

    }
}
