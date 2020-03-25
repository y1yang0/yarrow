package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class NewTypeArrayInstr extends StateInstr {
    private HirInstr len;
    private JavaKind elemType;

    public NewTypeArrayInstr(VmState stateBefore, HirInstr len, JavaKind elemType) {
        super(JavaKind.Object, stateBefore);
        this.len = len;
        this.elemType = elemType;
    }

    public HirInstr arrayLength() {
        return len;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: new {}[i{}]", super.id, elemType.getJavaName(), len.id);
    }
}
