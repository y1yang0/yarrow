package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.hir.instr.Instruction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecorateTarget {
    Class<? extends Instruction> klass();
}