package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class TypeCastInstr extends HirInstr {
    // TypeCastInstr is similar to C1's Convert instruciton
    private final int opcode;
    private final HirInstr from;
    private final JavaKind toType;

    public TypeCastInstr(int opcode, HirInstr from, JavaKind toType) {
        super(toType);
        this.opcode = opcode;
        this.from = from;
        this.toType = toType;
    }

    public int getOpcode() {
        return opcode;
    }

    public HirInstr getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: ({})i{}", super.id, toType.getJavaName(), from.id);
    }
}
