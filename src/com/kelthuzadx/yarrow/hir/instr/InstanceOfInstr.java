package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaType;

public class InstanceOfInstr extends StateInstr {
    private JavaType klass;
    private HirInstr object;

    public InstanceOfInstr(VmState stateBefore, JavaType klass, HirInstr object) {
        super(JavaKind.Int, stateBefore);
        this.klass = klass;
        this.object = object;
    }

    @Override
    public HirInstr ideal() {
        if(object instanceof ConstantInstr && ((ConstantInstr) object).getConstant().isNull()){
            return new ConstantInstr(JavaConstant.INT_0);
        }
        return this;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: i{} instanceof {}", super.id, object.id, klass.getUnqualifiedName());
    }
}
