package com.kelthuzadx.yarrow.core;

import jdk.vm.ci.code.CodeCacheProvider;
import jdk.vm.ci.code.RegisterConfig;
import jdk.vm.ci.code.TargetDescription;
import jdk.vm.ci.meta.ConstantReflectionProvider;
import jdk.vm.ci.meta.MetaAccessProvider;
import jdk.vm.ci.runtime.JVMCI;

public class YarrowRuntime {
    public static MetaAccessProvider metaAccess = JVMCI.getRuntime().getHostJVMCIBackend().getMetaAccess();

    public static CodeCacheProvider codeCache=JVMCI.getRuntime().getHostJVMCIBackend().getCodeCache();

    public static ConstantReflectionProvider constReflection=JVMCI.getRuntime().getHostJVMCIBackend().getConstantReflection();

    public static TargetDescription target=JVMCI.getRuntime().getHostJVMCIBackend().getTarget();

    public static RegisterConfig regConfig= JVMCI.getRuntime().getHostJVMCIBackend().getCodeCache().getRegisterConfig();


}
