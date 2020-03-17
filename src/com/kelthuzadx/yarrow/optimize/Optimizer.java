package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.hir.HIR;

public interface Optimizer{
    HIR optimize(HIR hir);
}
