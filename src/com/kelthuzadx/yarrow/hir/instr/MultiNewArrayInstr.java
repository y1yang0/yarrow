package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaType;

public class MultiNewArrayInstr extends Instruction {
    private JavaType klass;
    private Instruction[] dimenInstrs;

    public MultiNewArrayInstr(JavaType klass, Instruction[] dimenInstrs) {
        super(new Value(JavaKind.Object));
        this.klass = klass;
        this.dimenInstrs = dimenInstrs;
    }
}
