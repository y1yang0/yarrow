package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaType;

public class CheckCastInstr extends StateInstr {
    private final JavaType klass;
    private final HirInstr object;

    public CheckCastInstr(VmState stateBefore, JavaType klass, HirInstr object) {
        super(JavaKind.Object, stateBefore);
        this.klass = klass;
        this.object = object;
    }

    public HirInstr getObject() {
        return object;
    }

    public JavaType getKlass() {
        return klass;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: checkcast i{} -> {}", super.id, object.id, klass.getUnqualifiedName());
    }
}
