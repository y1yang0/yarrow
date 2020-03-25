package com.kelthuzadx.yarrow.lir.opcode;

import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.util.Logger;

import static com.kelthuzadx.yarrow.lir.operand.LirOperandFactory.illegalOperand;

public class JmpOpcode extends Op0Opcode {
    private Cond condition;
    private BlockStartInstr block;

    public JmpOpcode(Cond condition, BlockStartInstr block) {
        super(Mnemonic.JMP, illegalOperand());
        this.condition = condition;
        this.block = block;
    }


    @Override
    public String toString() {
        return Logger.format("i{}: jmp {}", super.id, block.id());
    }
}
