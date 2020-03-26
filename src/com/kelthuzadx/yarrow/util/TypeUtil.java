package com.kelthuzadx.yarrow.util;

import jdk.vm.ci.meta.JavaKind;

public class TypeUtil {
    public static JavaKind fromBasicType(int basicType) {
        switch (basicType) {
            case 4:
                return JavaKind.Boolean;
            case 5:
                return JavaKind.Char;
            case 6:
                return JavaKind.Float;
            case 7:
                return JavaKind.Double;
            case 8:
                return JavaKind.Byte;
            case 9:
                return JavaKind.Short;
            case 10:
                return JavaKind.Int;
            case 11:
                return JavaKind.Long;
            default:
                return JavaKind.Illegal;
        }
    }

    public static JavaKind decayType(JavaKind higher) {
        switch (higher) {
            case Void:
                return JavaKind.Void;
            case Boolean:
            case Byte:
            case Short:
            case Char:
            case Int:
                return JavaKind.Int;
            case Float:
                return JavaKind.Float;
            case Long:
                return JavaKind.Long;
            case Double:
                return JavaKind.Double;
            case Object:
                return JavaKind.Object;
            default:
                return JavaKind.Illegal;
        }
    }
}
