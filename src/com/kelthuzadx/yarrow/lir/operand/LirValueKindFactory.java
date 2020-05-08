package com.kelthuzadx.yarrow.lir.operand;

import com.kelthuzadx.yarrow.core.YarrowRuntime;
import jdk.vm.ci.amd64.AMD64;
import jdk.vm.ci.code.ValueKindFactory;
import jdk.vm.ci.meta.JavaKind;

public class LirValueKindFactory implements ValueKindFactory<LirValueKind> {
    @Override
    public LirValueKind getValueKind(JavaKind javaKind) {
        return new LirValueKind(YarrowRuntime.target.arch.getPlatformKind(javaKind));
    }
}
