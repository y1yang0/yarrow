package com.kelthuzadx.yarrow.core;


import com.kelthuzadx.yarrow.hir.CFG;
import com.kelthuzadx.yarrow.hir.HirBuilder;
import com.kelthuzadx.yarrow.lir.LirBuilder;
import com.kelthuzadx.yarrow.lir.regalloc.RegisterAlloc;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.code.CompilationRequest;
import jdk.vm.ci.code.CompilationRequestResult;
import jdk.vm.ci.hotspot.HotSpotCompilationRequestResult;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;
import jdk.vm.ci.runtime.JVMCICompiler;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YarrowCompiler implements JVMCICompiler {

    public YarrowCompiler() {
        YarrowRuntime.initialize();
    }

    @Override
    public CompilationRequestResult compileMethod(CompilationRequest request) {
        HotSpotResolvedJavaMethod method = (HotSpotResolvedJavaMethod) request.getMethod();
        if (method.hasCompiledCodeAtLevel(YarrowConfigAccess.access().CompLevel_full_optimization)) {
            return HotSpotCompilationRequestResult.success(0);
        }
        Logger.logf("=====Compiling {}.{}=====", method.getDeclaringClass().getUnqualifiedName(), method.getName());
        Stream.of(method)
                .map(CFG::new)
                .map(CFG::build)
                .peek(CFG::log)
                .map(HirBuilder::new)
                .map(HirBuilder::build)
                .peek(HirBuilder::log)
                .map(HirBuilder::getHir)
                .map(LirBuilder::new)
                .map(LirBuilder::build)
                .peek(LirBuilder::log)
                .map(LirBuilder::getLir)
                .map(RegisterAlloc::new)
                .map(RegisterAlloc::build)
                .collect(Collectors.toList());
        return HotSpotCompilationRequestResult.success(0);
    }
}
