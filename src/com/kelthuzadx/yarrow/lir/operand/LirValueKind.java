package com.kelthuzadx.yarrow.lir.operand;

import jdk.vm.ci.meta.PlatformKind;
import jdk.vm.ci.meta.ValueKind;

public class LirValueKind extends ValueKind<LirValueKind> {
    public LirValueKind(PlatformKind platformKind) {
        super(platformKind);
    }

    @Override
    public LirValueKind changeType(PlatformKind newPlatformKind) {
        return new LirValueKind(newPlatformKind);
    }
}
