package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.stub.NewArrayStub;
import com.kelthuzadx.yarrow.lir.stub.RuntimeStub;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

public class AllocateArrayInstr extends LirInstr {
    private NewArrayStub stub;
    private LirOperand klassReg;
    private LirOperand len;
    private LirOperand temp1;
    private LirOperand temp2;
    private LirOperand temp3;
    private LirOperand temp4;
    private JavaKind elementType;

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
