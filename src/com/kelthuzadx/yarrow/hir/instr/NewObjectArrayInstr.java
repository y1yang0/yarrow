package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaType;

public class NewObjectArrayInstr extends StateInstr {
    private HirInstruction len;
    private JavaType klass;

    public NewObjectArrayInstr(VmState stateBefore, HirInstruction len, JavaType klass) {
        super(new Value(JavaKind.Object), stateBefore);
        this.len = len;
        this.klass = klass;
    }

    public HirInstruction arrayLength() {
        return len;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: new {}[i{}]", super.id, klass.getUnqualifiedName(), len.id);
    }
}
