package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.hir.HIR;
import com.kelthuzadx.yarrow.phase.Phase;

public class LIRBuilder implements Phase {
    private final HIR hir;
    private LIR lir;

    public LIRBuilder(HIR hir){
        this.hir =hir;
        this.lir = new LIR();
    }

    @Override
    public Phase build() {
        return this;
    }

    @Override
    public String name() {
        return "Create Low level IR";
    }

    @Override
    public void log() {

    }
}
