package com.kelthuzadx.yarrow.core;

import jdk.vm.ci.code.Architecture;
import jdk.vm.ci.code.CodeCacheProvider;
import jdk.vm.ci.code.RegisterConfig;
import jdk.vm.ci.code.TargetDescription;
import jdk.vm.ci.meta.ConstantReflectionProvider;
import jdk.vm.ci.meta.MetaAccessProvider;
import jdk.vm.ci.runtime.JVMCI;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class YarrowRuntime {
    public static YarrowConfigAccess access = YarrowConfigAccess.access();

    public static MetaAccessProvider metaAccess = JVMCI.getRuntime().getHostJVMCIBackend().getMetaAccess();

    public static CodeCacheProvider codeCache = JVMCI.getRuntime().getHostJVMCIBackend().getCodeCache();

    public static ConstantReflectionProvider constReflection = JVMCI.getRuntime().getHostJVMCIBackend().getConstantReflection();

    public static TargetDescription target = JVMCI.getRuntime().getHostJVMCIBackend().getTarget();

    public static Architecture arch = target.arch;

    public static RegisterConfig regConfig = JVMCI.getRuntime().getHostJVMCIBackend().getCodeCache().getRegisterConfig();

    public static Unsafe unsafe;

    public static void initialize() {
        Field f;
        try {
            f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
