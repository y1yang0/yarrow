package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import jdk.vm.ci.meta.JavaKind;

public class LoadIndexInstr extends AccessArrayInstr {
    private Instruction index;
    private Instruction length;
    private JavaKind elementType;

    public LoadIndexInstr(Instruction array, Instruction index, Instruction length, JavaKind elementType){
        super(new Value(),array);
        this.index = index;
        this.length = length;
        this.elementType = elementType;
    }

}
