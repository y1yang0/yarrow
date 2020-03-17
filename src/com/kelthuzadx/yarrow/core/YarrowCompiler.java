package com.kelthuzadx.yarrow.core;


import com.kelthuzadx.yarrow.hir.HIR;
import com.kelthuzadx.yarrow.hir.HIRBuilder;
import com.kelthuzadx.yarrow.optimize.Ideal;
import com.kelthuzadx.yarrow.optimize.Optimizer;
import com.kelthuzadx.yarrow.util.Logger;
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
        if (method.hasCompiledCodeAtLevel(YarrowConfigAccess.CompLevel_full_optimization)) {
            return HotSpotCompilationRequestResult.success(0);
        }
        Logger.logf("=====Compiling {}.{}=====", method.getDeclaringClass().getUnqualifiedName(), method.getName());
        HIR hir = new HIRBuilder(method).build();
        Optimizer opt = new Ideal();
        opt.optimize(hir);
        System.exit(0);
        return HotSpotCompilationRequestResult.success(0);
    }

    public JVMCIRuntime getRuntime() {
        return runtime;
    }
}
