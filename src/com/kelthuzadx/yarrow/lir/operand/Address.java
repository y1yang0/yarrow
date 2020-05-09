package com.kelthuzadx.yarrow.lir.operand;

import com.kelthuzadx.yarrow.core.YarrowError;
import jdk.vm.ci.meta.AllocatableValue;
import jdk.vm.ci.meta.JavaKind;

public class Address extends AllocatableValue {
    private final AllocatableValue base;
    private final AllocatableValue index;
    private final int scale;
    private final int displacement;
    private final JavaKind type;

    // displacement[base+index*scale]
    public Address(AllocatableValue base, AllocatableValue index, int scale, int displacement, JavaKind type) {
        super(new LirValueKindFactory().getValueKind(type));
        this.base = base;
        this.index = index;
        this.scale = scale;
        this.displacement = displacement;
        this.type = type;
    }

    // displacement[base+index*1]
    public Address(AllocatableValue base, AllocatableValue index, int displacement, JavaKind type) {
        this(base, index, 1, displacement, type);
    }

    // [base+index*1]
    public Address(AllocatableValue base, AllocatableValue index, JavaKind type) {
        this(base, index, 1, 0, type);
    }

    // displacement[base]
    public Address(AllocatableValue base, int displacement, JavaKind type) {
        this(base, AllocatableValue.ILLEGAL, 1, displacement, type);
    }

    // [base]
    public Address(AllocatableValue base, JavaKind type) {
        this(base, AllocatableValue.ILLEGAL, 1, 0, type);
    }

    public static int scaleFor(JavaKind type) {
        switch (type) {
            case Boolean:
            case Byte:
                return 1;
            case Char:
            case Short:
                return 2;
            case Float:
            case Int:
                return 4;
            case Double:
            case Long:
            case Object:
                return 8;
            default:
                YarrowError.shouldNotReachHere();
        }
        return Integer.MAX_VALUE;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (displacement != 0) {
            result.append(displacement);
        }
        result.append("[").append(base);
        if (index != ILLEGAL) {
            result.append("+" + index);
            if (scale != 1) {
                result.append("*" + scale);
            }
        }
        result.append("]");

        return result.toString();
    }
}
