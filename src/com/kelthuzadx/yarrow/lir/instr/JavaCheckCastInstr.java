package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.lir.stub.ClassCastExStub;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaType;

public class JavaCheckCastInstr extends LirInstr {
    private final LirOperand object;
    private final HotSpotResolvedJavaType klassType;
    private final ClassCastExStub stub;

    public JavaCheckCastInstr(LirOperand result, LirOperand object, HotSpotResolvedJavaType klassType, ClassCastExStub stub) {
        super(Mnemonic.CHECK_CAST, result);
        this.object = object;
        this.klassType = klassType;
        this.stub = stub;
    }


    @Override
    public String toString() {
        return Logger.format("i{}: cast_obj {},{}", super.id, object.toString(), klassType.getName());
    }
}
