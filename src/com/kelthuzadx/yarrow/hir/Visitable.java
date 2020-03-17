package com.kelthuzadx.yarrow.hir;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public interface Visitable {
    default void visit(InstructionVisitor visitor) {
        MethodType mt = MethodType.methodType(void.class, this.getClass());
        try {
            MethodHandle mh = MethodHandles.lookup().findVirtual(visitor.getClass(), "visit" + this.getClass().getSimpleName(), mt);
            mh.invoke(visitor, this);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
