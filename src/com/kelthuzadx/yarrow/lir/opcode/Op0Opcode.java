package com.kelthuzadx.yarrow.lir.opcode;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class Op0Opcode extends LirOpcode {

    public Op0Opcode(Mnemonic mnemonic, LirOperand result) {
        super(mnemonic, result);
    }

    @Override
    public String toString() {
        switch (mnemonic) {
            case Membar:
                return Logger.format("i{}: membar full", super.id);
            case MembarLoadLoad:
                return Logger.format("i{}: membar LoadLoad", super.id);
            case MembarLoadStore:
                return Logger.format("i{}: membar LoadStore", super.id);
            case MembarStoreLoad:
                return Logger.format("i{}: membar StoreLoad", super.id);
            case MembarStoreStore:
                return Logger.format("i{}: membar StoreStore", super.id);
            case MembarAcquire:
                return Logger.format("i{}: membar Acquire", super.id);
            case MembarRelease:
                return Logger.format("i{}: membar Release", super.id);
            case OsrEntry:
                return Logger.format("i{}: osr_entry", super.id);
            case NormalEntry:
                return Logger.format("i{}: normal_entry", super.id);
            default:
                break;
        }
        return Logger.format("i{}: {} {}", super.id, mnemonic.name().toLowerCase(), result.toString());

    }
}
