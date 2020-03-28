package com.kelthuzadx.yarrow.core;

import jdk.vm.ci.hotspot.HotSpotJVMCIRuntime;
import jdk.vm.ci.hotspot.HotSpotVMConfigAccess;
import jdk.vm.ci.hotspot.HotSpotVMConfigStore;
import jdk.vm.ci.runtime.JVMCI;

public class YarrowConfigAccess extends HotSpotVMConfigAccess {
    private static YarrowConfigAccess access;

    public final int CompLevel_full_optimization = getConstant("CompLevel_full_optimization",
            Integer.class);

    public final boolean useCompressedClassPointers = getFlag("UseCompressedClassPointers", Boolean.class);

    public final int sizeofArrayOopDesc = getFieldValue("CompilerToVM::Data::sizeof_arrayOopDesc", Integer.class, "int");

    public final int sizeofNarrowKlass = getFieldValue("CompilerToVM::Data::sizeof_narrowKlass", Integer.class, "int");

    public final int hubOffset = getFieldOffset("oopDesc::_metadata._klass", Integer.class, "Klass*");


    private YarrowConfigAccess(HotSpotVMConfigStore store) {
        super(store);
    }

    public static YarrowConfigAccess access() {
        if (access == null) {
            access = new YarrowConfigAccess(((HotSpotJVMCIRuntime) JVMCI.getRuntime()).getConfigStore());
        }
        return access;
    }

    public int getArrayLengthOffset() {
        if (useCompressedClassPointers) {
            return sizeofNarrowKlass + hubOffset;
        } else {
            return sizeofArrayOopDesc;
        }
    }
}
