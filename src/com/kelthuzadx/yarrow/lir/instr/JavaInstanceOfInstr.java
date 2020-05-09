package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaType;
import jdk.vm.ci.meta.AllocatableValue;

public class JavaInstanceOfInstr extends LirInstr {
    private final AllocatableValue object;
    private final HotSpotResolvedJavaType klassType;

    public JavaInstanceOfInstr(AllocatableValue result, AllocatableValue object, HotSpotResolvedJavaType klassType) {
        super(Mnemonic.INSTANCE_OF, result);
        this.object = object;
        this.klassType = klassType;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: type_check {},{}", super.id, stringify(object), klassType.getName());
    }
}
