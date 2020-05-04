package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaType;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;

public class InstanceOfInstr extends StateInstr {
    private final HotSpotResolvedJavaType klass;
    private final HirInstr object;

    public InstanceOfInstr(VmState stateBefore, HotSpotResolvedJavaType klass, HirInstr object) {
        super(JavaKind.Int, stateBefore);
        this.klass = klass;
        this.object = object;
    }

    public HirInstr getObject() {
        return object;
    }

    public HotSpotResolvedJavaType getKlass() {
        return klass;
    }

    @Override
    public HirInstr ideal() {
        if (object instanceof ConstantInstr && ((ConstantInstr) object).getConstant().isNull()) {
            return new ConstantInstr(JavaConstant.INT_0);
        }
        return this;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: i{} instanceof {}", super.id, object.id, klass.getUnqualifiedName());
    }
}
