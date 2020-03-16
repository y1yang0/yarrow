package com.kelthuzadx.yarrow.core;

import jdk.vm.ci.hotspot.HotSpotJVMCIRuntime;
import jdk.vm.ci.hotspot.HotSpotVMConfigAccess;
import jdk.vm.ci.hotspot.HotSpotVMConfigStore;
import jdk.vm.ci.runtime.JVMCI;

public class YarrowConfigAccess extends HotSpotVMConfigAccess {
    private static final HotSpotVMConfigAccess access = new HotSpotVMConfigAccess(((HotSpotJVMCIRuntime) JVMCI.getRuntime()).getConfigStore());
    public static final int CompLevel_full_optimization = access.getConstant("CompLevel_full_optimization",
            Integer.class);

    private YarrowConfigAccess(HotSpotVMConfigStore store) {
        super(store);
    }
}
