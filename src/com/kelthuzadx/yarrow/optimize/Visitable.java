package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.util.Logger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static com.kelthuzadx.yarrow.core.YarrowProperties.Debug.TraceLIRGeneration;

public interface Visitable {
    default void visit(InstructionVisitor visitor) {
        MethodType mt = MethodType.methodType(void.class, this.getClass());
        try {
            MethodHandle mh = MethodHandles.lookup().findVirtual(visitor.getClass(), "visit" + this.getClass().getSimpleName(), mt);
            if (TraceLIRGeneration) {
                Logger.logf("====={}=====>", this.getClass().getSimpleName());
            }
            mh.invoke(visitor, this);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
