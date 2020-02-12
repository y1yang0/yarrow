package com.kelthuzadx.yarrow.service;

import com.kelthuzadx.yarrow.core.YarrowCompilerFactory;
import jdk.vm.ci.runtime.JVMCICompilerFactory;
import jdk.vm.ci.services.JVMCIServiceLocator;

public class YarrowServiceLocator extends JVMCIServiceLocator {
    @Override
    protected <S> S getProvider(Class<S> service) {
        if (service == JVMCICompilerFactory.class) {
            return (S) new YarrowCompilerFactory();
        }
        return null;
    }
}
