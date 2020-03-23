package com.kelthuzadx.yarrow.lir.opcode;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;

public class LirOpcode {
    protected int id;
    protected Mnemonic mnemonic;
    protected LirOperand result;

    public LirOpcode(Mnemonic mnemonic, LirOperand result) {
        this.id = IdGenerator.next();
        this.mnemonic = mnemonic;
        this.result = result;
    }

    /**
     * Simple ID generator
     *
     * @for HIR
     */
    private static class IdGenerator {
        private static int id;

        static int next() {
            return id++;
        }
    }
}
