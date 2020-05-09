package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.AllocatableValue;

public class Op2Instr extends LirInstr {
    private final Cond cond;
    private final AllocatableValue leftOperand;
    private final AllocatableValue rightOperand;

    public Op2Instr(Mnemonic mnemonic, AllocatableValue result, AllocatableValue leftOperand, AllocatableValue rightOperand) {
        super(mnemonic, result);
        this.cond = null;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public Op2Instr(Mnemonic mnemonic, Cond cond, AllocatableValue result, AllocatableValue leftOperand, AllocatableValue rightOperand) {
        super(mnemonic, result);
        this.cond = cond;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public String toString() {
        switch (mnemonic) {
            case CMP:
                return Logger.format("i{}: cmp_{} {},{}", super.id, cond.name().toLowerCase(),
                        stringify(leftOperand), stringify(rightOperand));
        }
        return Logger.format("i{}: {} {} {},{}", super.id, mnemonic.name().toLowerCase(), stringify(result),
                stringify(leftOperand), stringify(rightOperand));

    }
}
