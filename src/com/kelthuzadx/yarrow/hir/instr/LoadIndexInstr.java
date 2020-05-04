package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

import java.util.Objects;

public class LoadIndexInstr extends AccessArrayInstr {
    private final HirInstr index;
    private final HirInstr length;
    private final JavaKind elementType;

    public LoadIndexInstr(HirInstr array, HirInstr index, HirInstr length, JavaKind elementType) {
        super(elementType, array);
        this.index = index;
        this.length = length;
        this.elementType = elementType;
    }

    public HirInstr getIndex() {
        return index;
    }

    public JavaKind getElementType() {
        return elementType;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: i{}[i{}] [{}]", super.id, super.array.id, index.id, elementType.getJavaName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoadIndexInstr)) return false;
        var that = (LoadIndexInstr) o;
        return array.equals(that.array) && index.equals(that.index) && elementType == that.elementType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(array, index, elementType);
    }
}
