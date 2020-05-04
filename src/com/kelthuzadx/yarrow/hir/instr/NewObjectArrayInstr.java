package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaType;

public class NewObjectArrayInstr extends StateInstr {
    private final HirInstr len;
    private final JavaType klass;

    public NewObjectArrayInstr(VmState stateBefore, HirInstr len, JavaType klass) {
        super(JavaKind.Object, stateBefore);
        this.len = len;
        this.klass = klass;
    }

    public JavaType getKlass() {
        return klass;
    }

    public HirInstr arrayLength() {
        return len;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: new {}[i{}]", super.id, klass.getUnqualifiedName(), len.id);
    }
}
