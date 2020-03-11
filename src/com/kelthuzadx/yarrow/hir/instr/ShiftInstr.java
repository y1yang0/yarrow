package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;

public class ShiftInstr extends Op2Instr{
    public ShiftInstr(int opcode, Instruction left, Instruction right) {
        super(new Value(left.getType()),opcode, left, right);
    }
}
