package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaType;

public class NewObjectArrayInstr extends StateInstr {
    private Instruction len;
    private JavaType klass;

    public NewObjectArrayInstr(VmState stateBefore, Instruction len, JavaType klass) {
        super(new Value(JavaKind.Object), stateBefore);
        this.len = len;
        this.klass = klass;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: new_objarr #{}", super.id, klass.getName());
    }
}
