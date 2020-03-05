package com.kelthuzadx.yarrow.ir;

import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;

public class GraphBuilder {
    private HotSpotResolvedJavaMethod method;

    public GraphBuilder(HotSpotResolvedJavaMethod method) {
        this.method = method;
    }

    public GraphBuilder build() {
        CFG cfg = CFG.build(method);

        return this;
    }
}
