package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.stub.NewArrayStub;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.AllocatableValue;
import jdk.vm.ci.meta.JavaKind;

public class AllocateArrayInstr extends LirInstr {
    private final NewArrayStub stub;
    private final AllocatableValue klassReg;
    private final AllocatableValue length;
    private final AllocatableValue temp1;
    private final AllocatableValue temp2;
    private final AllocatableValue temp3;
    private final AllocatableValue temp4;
    private final JavaKind elementType;

    public AllocateArrayInstr(NewArrayStub stub, AllocatableValue klassReg, AllocatableValue dest, AllocatableValue length, AllocatableValue temp1, AllocatableValue temp2, AllocatableValue temp3, AllocatableValue temp4, JavaKind elementType) {
        super(Mnemonic.ALLOCATE_ARRAY, dest);
        this.stub = stub;
        this.klassReg = klassReg;
        this.length = length;
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.temp3 = temp3;
        this.temp4 = temp4;
        this.elementType = elementType;
    }

    public AllocatableValue getKlassReg() {
        return klassReg;
    }

    public AllocatableValue getLength() {
        return length;
    }

    public AllocatableValue getTemp1() {
        return temp1;
    }

    public AllocatableValue getTemp2() {
        return temp2;
    }

    public AllocatableValue getTemp3() {
        return temp3;
    }

    public AllocatableValue getTemp4() {
        return temp4;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: alloc_array {}*{}", super.id, elementType.getJavaName(), stringify(length));

    }
}
