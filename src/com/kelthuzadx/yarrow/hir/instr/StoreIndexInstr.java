package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class StoreIndexInstr extends AccessArrayInstr {
    private Instruction index;
    private Instruction length;
    private JavaKind elementType;
    private Instruction value;

    public StoreIndexInstr(Instruction array, Instruction index, Instruction length, JavaKind elementType, Instruction value) {
        super(new Value(elementType), array);
        this.index = index;
        this.length = length;
        this.elementType = elementType;
        this.value = value;
    }

    @Override
    public String toString() {
        return Logger.f("i{}: store i{}[i{}]#{},i{}", super.id, super.array, index.id, elementType.getJavaName(), value.id);
    }
}
