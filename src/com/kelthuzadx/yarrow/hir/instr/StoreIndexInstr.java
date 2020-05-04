package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class StoreIndexInstr extends AccessArrayInstr {
    private final HirInstr index;
    private final HirInstr length;
    private final JavaKind elementType;
    private final HirInstr value;

    public StoreIndexInstr(HirInstr array, HirInstr index, HirInstr length, JavaKind elementType, HirInstr value) {
        super(elementType, array);
        this.index = index;
        this.length = length;
        this.elementType = elementType;
        this.value = value;
    }

    public HirInstr getIndex() {
        return index;
    }

    public JavaKind getElementType() {
        return elementType;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: i{}[i{}] = i{} [{}]", super.id, super.array.id, index.id, value.id, elementType.getJavaName());
    }
}
