package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.lir.stub.RuntimeStub;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;


public class BranchInstr extends Op0Instr {
    private final Cond condition;
    private final JavaKind type;
    private final RuntimeStub stub;
    private final BlockStartInstr block;

    public BranchInstr(Mnemonic mnemonic, LirOperand result, Cond condition, JavaKind type, RuntimeStub stub, BlockStartInstr block) {
        super(mnemonic, result);
        this.condition = condition;
        this.type = type;
        this.stub = stub;
        this.block = block;
    }

    public BranchInstr(Cond condition, JavaKind type, BlockStartInstr block) {
        this(Mnemonic.BRANCH, LirOperand.illegal, condition, type, null, block);
    }

    public BranchInstr(Cond condition, BlockStartInstr block) {
        this(Mnemonic.BRANCH, LirOperand.illegal, condition, null, null, block);
    }

    public BranchInstr(Cond condition, RuntimeStub stub) {
        this(Mnemonic.BRANCH, LirOperand.illegal, condition, null, stub, null);
    }


    @Override
    public String toString() {
        if (condition == Cond.Always) {
            return Logger.format("i{}: jmp L{}", super.id, block == null ? stub.toString() : block.id());
        } else {
            return Logger.format("i{}: branch_{} L{}", super.id, condition.name().toLowerCase()
                    , block == null ? stub.toString() : block.id());
        }
    }
}
