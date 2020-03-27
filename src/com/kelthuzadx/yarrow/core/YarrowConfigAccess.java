package com.kelthuzadx.yarrow.core;

import jdk.vm.ci.hotspot.HotSpotJVMCIRuntime;
import jdk.vm.ci.hotspot.HotSpotVMConfigAccess;
import jdk.vm.ci.hotspot.HotSpotVMConfigStore;
import jdk.vm.ci.runtime.JVMCI;

public class YarrowConfigAccess extends HotSpotVMConfigAccess {
    private static YarrowConfigAccess access = new YarrowConfigAccess(((HotSpotJVMCIRuntime) JVMCI.getRuntime()).getConfigStore());
    public static final int CompLevel_full_optimization = access.getConstant("CompLevel_full_optimization",
            Integer.class);

    private YarrowConfigAccess(HotSpotVMConfigStore store) {
        super(store);
    }

    public static YarrowConfigAccess access(){
        return access;
    }
}
