package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class ArrayLenInstr extends AccessArrayInstr {

    public ArrayLenInstr(Instruction array) {
        super(new Value(JavaKind.Int), array);
    }

    @Override
    public Instruction ideal() {
        // Object[] arr = new Object[3];
        // int p = arr.length;
        if(array instanceof NewTypeArrayInstr){
            Instruction lenInstr = ((NewTypeArrayInstr) array).arrayLength();
            if(lenInstr instanceof ConstantInstr){
                int len = ((ConstantInstr)lenInstr).value();
                return new ConstantInstr(new Value(JavaKind.Int,len));
            }
        }else if(array instanceof NewObjectArrayInstr){
            Instruction lenInstr = ((NewObjectArrayInstr) array).arrayLength();
            if(lenInstr instanceof ConstantInstr){
                int len = ((ConstantInstr)lenInstr).value();
                return new ConstantInstr(new Value(JavaKind.Int,len));
            }
        }
        return super.ideal();
    }

    @Override
    public String toString() {
        return Logger.format("i{}: arraylen i{}", super.id, super.array.id);
    }
}
