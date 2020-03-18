package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.hir.HIR;
import com.kelthuzadx.yarrow.phase.Phase;

public class GVN implements Phase {
    private HIR hir;

    private GVN(HIR hir) {
        this.hir = hir;
    }

    @Override
    public Phase build() {
        return null;
    }

    @Override
    public String name() {
        return "Global Value Numbering";
    }

    @Override
    public void log() {

    }
}
