package com.kelthuzadx.yarrow.core;

import jdk.vm.ci.code.Architecture;
import jdk.vm.ci.code.CodeCacheProvider;
import jdk.vm.ci.code.RegisterConfig;
import jdk.vm.ci.code.TargetDescription;
import jdk.vm.ci.hotspot.HotSpotJVMCIRuntime;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaType;
import jdk.vm.ci.meta.ConstantReflectionProvider;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.MetaAccessProvider;
import jdk.vm.ci.runtime.JVMCI;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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


    public static long getKlassPointer(Class<?> javaClass) {
        int klassOffset = access.klassOffset;
        if (HotSpotJVMCIRuntime.getHostWordKind() == JavaKind.Long) {
            return unsafe.getLong(javaClass, klassOffset);
        }
        return unsafe.getInt(javaClass, klassOffset) & 0xFFFFFFFFL;
    }

    public static long getKlassPointer(HotSpotResolvedJavaType klass) {
        Class<?> javaClass = null;

        try {
            Method m = klass.getClass().getMethod("mirror");
            m.setAccessible(true);
            javaClass = (Class<?>) m.invoke(klass);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return getKlassPointer(javaClass);
    }
}
