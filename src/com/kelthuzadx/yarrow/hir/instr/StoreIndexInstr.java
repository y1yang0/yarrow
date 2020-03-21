package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class StoreIndexInstr extends AccessArrayInstr {
    private HirInstruction index;
    private HirInstruction length;
    private JavaKind elementType;
    private HirInstruction value;

    public StoreIndexInstr(HirInstruction array, HirInstruction index, HirInstruction length, JavaKind elementType, HirInstruction value) {
        super(new Value(elementType), array);
        this.index = index;
        this.length = length;
        this.elementType = elementType;
        this.value = value;
    }

    public HirInstruction getIndex() {
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
