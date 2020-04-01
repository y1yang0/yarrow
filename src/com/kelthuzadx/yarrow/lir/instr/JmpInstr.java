package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.stub.RuntimeStub;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;


public class JmpInstr extends Op0Instr {
    private Cond condition;
    private RuntimeStub stub;
    private BlockStartInstr block;

    public JmpInstr(Cond condition, BlockStartInstr block) {
        super(Mnemonic.JMP, LirOperand.illegal);
        this.condition = condition;
        this.block = block;
    }

    public JmpInstr(Cond condition, RuntimeStub stub) {
        super(Mnemonic.JMP, LirOperand.illegal);
        this.condition = condition;
        this.block = null;
        this.stub = stub;
    }


    @Override
    public String toString() {
        return Logger.format("i{}: jmp {}", super.id, block == null ? stub.toString() : block.id());
    }
}
