package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;

public class LirInstr {
    protected int id;
    protected Opcode opcode;
    protected LirOperand result;

    public LirInstr(Opcode opcode, LirOperand result) {
        this.id = IdGenerator.next();
        this.opcode = opcode;
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
