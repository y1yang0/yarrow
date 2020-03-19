package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

import java.util.Objects;

public class LoadIndexInstr extends AccessArrayInstr {
    private Instruction index;
    private Instruction length;
    private JavaKind elementType;

    public LoadIndexInstr(Instruction array, Instruction index, Instruction length, JavaKind elementType) {
        super(new Value(elementType), array);
        this.index = index;
        this.length = length;
        this.elementType = elementType;
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
