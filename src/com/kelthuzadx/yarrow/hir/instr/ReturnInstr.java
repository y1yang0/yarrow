package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

import java.util.ArrayList;

public class ReturnInstr extends BlockEndInstr {
    private HirInstr returnValue;

    public ReturnInstr(HirInstr returnValue) {
        super(new Value(returnValue == null ? JavaKind.Void : returnValue.type()),
                null,
                new ArrayList<>());
        this.returnValue = returnValue;
    }

    public HirInstr getReturnValue() {
        return returnValue;
    }

    @Override
    public String toString() {
        if (returnValue == null) {
            return Logger.format("i{}: return", super.id);
        }
        return Logger.format("i{}: return i{}", super.id, returnValue.id);
    }
}
