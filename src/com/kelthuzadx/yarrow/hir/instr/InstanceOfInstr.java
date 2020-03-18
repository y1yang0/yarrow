package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaType;

public class InstanceOfInstr extends StateInstr {
    private JavaType klass;
    private Instruction object;

    public InstanceOfInstr(VmState stateBefore, JavaType klass, Instruction object) {
        super(new Value(JavaKind.Int), stateBefore);
        this.klass = klass;
        this.object = object;
    }

    @Override
    public Instruction ideal() {
        if(object instanceof ConstantInstr && object.isType(JavaKind.Object) && object.value()==null){
            return new ConstantInstr(new Value(JavaKind.Int,0));
        }
        return this;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: i{} instanceof {}", super.id, object.id, klass.getUnqualifiedName());
    }
}
