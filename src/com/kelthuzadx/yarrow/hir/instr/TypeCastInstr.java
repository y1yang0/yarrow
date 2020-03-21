package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class TypeCastInstr extends HirInstruction {
    private int opcode;
    private HirInstruction from;
    private JavaKind toType;

    public TypeCastInstr(int opcode, HirInstruction from, JavaKind toType) {
        super(new Value(toType));
        this.opcode = opcode;
        this.from = from;
        this.toType = toType;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: ({})i{}", super.id, toType.getJavaName(), from.id);
    }
}
