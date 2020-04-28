package com.kelthuzadx.yarrow.lir.operand;

import jdk.vm.ci.code.ValueKindFactory;
import jdk.vm.ci.meta.JavaKind;

public class LirValueKindFactory implements ValueKindFactory<LirValueKind> {
    @Override
    public LirValueKind getValueKind(JavaKind javaKind) {
        return null;
    }
}
