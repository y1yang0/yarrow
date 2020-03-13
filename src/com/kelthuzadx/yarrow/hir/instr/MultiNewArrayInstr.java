package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaType;

public class MultiNewArrayInstr extends StateInstr {
    private JavaType klass;
    private Instruction[] dimenInstrs;

    public MultiNewArrayInstr(VmState stateBefore, JavaType klass, Instruction[] dimenInstrs) {
        super(new Value(JavaKind.Object), stateBefore);
        this.klass = klass;
        this.dimenInstrs = dimenInstrs;
    }
}
