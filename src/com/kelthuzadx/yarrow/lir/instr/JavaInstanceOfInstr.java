package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.lir.operand.LirOperand;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaType;

public class JavaInstanceOfInstr extends LirInstr {
    private final LirOperand object;
    private final HotSpotResolvedJavaType klassType;

    public JavaInstanceOfInstr(LirOperand result, LirOperand object, HotSpotResolvedJavaType klassType) {
        super(Mnemonic.INSTANCE_OF, result);
        this.object = object;
        this.klassType = klassType;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: type_check {},{}", super.id, object.toString(), klassType.getName());
    }
}
