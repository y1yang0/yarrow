package com.kelthuzadx.yarrow.core;


import com.kelthuzadx.yarrow.hir.HirBuilder;
import jdk.vm.ci.code.CompilationRequest;
import jdk.vm.ci.code.CompilationRequestResult;
import jdk.vm.ci.hotspot.HotSpotCompilationRequestResult;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;
import jdk.vm.ci.runtime.JVMCICompiler;
import jdk.vm.ci.runtime.JVMCIRuntime;

public class YarrowCompiler implements JVMCICompiler {

    private JVMCIRuntime runtime;

    YarrowCompiler(JVMCIRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public CompilationRequestResult compileMethod(CompilationRequest request) {
        HotSpotResolvedJavaMethod method = (HotSpotResolvedJavaMethod) request.getMethod();
        if (method.hasCompiledCodeAtLevel(YarrowHotSpotConfigAccess.CompLevel_full_optimization)) {
            return HotSpotCompilationRequestResult.success(0);
        }
        new HirBuilder(method).build();
        System.exit(0);
        return HotSpotCompilationRequestResult.success(0);
    }

    public JVMCIRuntime getRuntime() {
        return runtime;
    }
}
