package com.kelthuzadx.yarrow.util;

import jdk.vm.ci.meta.JavaKind;

public class Converter {
    public static JavaKind fromBasicType(int basicType){
        switch (basicType){
            case 4:return JavaKind.Boolean;
            case 5:return JavaKind.Char;
            case 6:return JavaKind.Float;
            case 7:return JavaKind.Double;
            case 8:return JavaKind.Byte;
            case 9:return JavaKind.Short;
            case 10:return JavaKind.Int;
            case 11:return JavaKind.Long;
            default:return JavaKind.Illegal;
        }
    }
}
