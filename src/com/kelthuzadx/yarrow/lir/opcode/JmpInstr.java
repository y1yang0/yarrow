package com.kelthuzadx.yarrow.lir.opcode;

import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;


public class JmpInstr extends Op0Instr {
    private Cond condition;
    private BlockStartInstr block;

    public JmpInstr(Cond condition, BlockStartInstr block) {
        super(Mnemonic.JMP, LirOperand.illegal);
        this.condition = condition;
        this.block = block;
    }


    @Override
    public String toString() {
        return Logger.format("i{}: jmp {}", super.id, block.id());
    }
}
