package com.kelthuzadx.yarrow.core;

import com.kelthuzadx.yarrow.util.Logger;
import com.kelthuzadx.yarrow.util.TimingTracer;
import jdk.vm.ci.hotspot.HotSpotJVMCICompilerFactory;
import jdk.vm.ci.runtime.JVMCICompiler;
import jdk.vm.ci.runtime.JVMCIRuntime;

import java.io.PrintStream;

public class YarrowCompilerFactory extends HotSpotJVMCICompilerFactory {

    public YarrowCompilerFactory() {
    }


    @Override
    public String getCompilerName() {
        return "yarrow";
    }

    @Override
    public void onSelection() {
        try {
            Class.forName("com.kelthuzadx.yarrow.core.YarrowProperties");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JVMCICompiler createCompiler(JVMCIRuntime runtime) {
        return new YarrowCompiler(runtime);
    }

    @Override
    public void printProperties(PrintStream out) {
        //TODO: Prints a description of the properties used to configure this compiler.
    }
}