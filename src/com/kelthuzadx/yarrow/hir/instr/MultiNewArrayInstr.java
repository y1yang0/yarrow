package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
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

    @Override
    public String toString() {
        String typeStr = klass.toJavaName();
        for (Instruction dimen : dimenInstrs) {
            typeStr = typeStr.replaceFirst("\\[\\]", "[i" + dimen.id + "]");
        }
        return Logger.format("i{}: new {}", super.id, typeStr);
    }
}
