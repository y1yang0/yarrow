package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.AllocatableValue;

public class Op1Instr extends LirInstr {
    protected AllocatableValue operand;

    public Op1Instr(Mnemonic mnemonic, AllocatableValue result, AllocatableValue operand) {
        super(mnemonic, result);
        this.operand = operand;
    }

    public AllocatableValue operand1() {
        return operand;
    }

    @Override
    public String toString() {
        switch (mnemonic) {
            case RETURN:
                return Logger.format("i{}: return {}", super.id, (operand == null || operand == AllocatableValue.ILLEGAL) ? "" :
                        stringify(operand));
        }
        return Logger.format("i{}: {} {},{}", super.id, mnemonic.name().toLowerCase(), stringify(result),
                stringify(operand));

    }
}
