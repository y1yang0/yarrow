package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaType;

public class NewMultiArrayInstr extends StateInstr {
    private JavaType klass;
    private HirInstr[] sizeArray;
    private HirInstr len;

    public NewMultiArrayInstr(VmState stateBefore, JavaType klass, HirInstr[] sizeArray) {
        super(JavaKind.Object, stateBefore);
        this.klass = klass;
        this.sizeArray = sizeArray;
        this.len = sizeArray[sizeArray.length - 1];
    }

    public HirInstr[] getSizeArray() {
        return sizeArray;
    }

    public HirInstr arrayLength() {
        return len;
    }

    @Override
    public String toString() {
        String typeStr = klass.toJavaName();
        for (HirInstr dimen : sizeArray) {
            typeStr = typeStr.replaceFirst("\\[\\]", "[i" + dimen.id + "]");
        }
        return Logger.format("i{}: new {}", super.id, typeStr);
    }
}
