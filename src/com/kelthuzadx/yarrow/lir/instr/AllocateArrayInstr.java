package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.lir.stub.NewArrayStub;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class AllocateArrayInstr extends LirInstr {
    private final NewArrayStub stub;
    private final LirOperand klassReg;
    private final LirOperand len;
    private final LirOperand temp1;
    private final LirOperand temp2;
    private final LirOperand temp3;
    private final LirOperand temp4;
    private final JavaKind elementType;

    public AllocateArrayInstr(NewArrayStub stub, LirOperand klassReg, LirOperand dest, LirOperand len, LirOperand temp1, LirOperand temp2, LirOperand temp3, LirOperand temp4, JavaKind elementType) {
        super(Mnemonic.AllocateArray, dest);
        this.stub = stub;
        this.klassReg = klassReg;
        this.len = len;
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.temp3 = temp3;
        this.temp4 = temp4;
        this.elementType = elementType;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: alloc_array {}*{}", super.id, elementType.getJavaName(), len);

    }
}
