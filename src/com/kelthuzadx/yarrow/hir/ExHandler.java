package com.kelthuzadx.yarrow.hir;

import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import jdk.vm.ci.meta.ExceptionHandler;
import jdk.vm.ci.meta.JavaType;

public class ExHandler {
    //[startBci,endBci)
    private final int startBci;
    private final int endBci;
    private final JavaType catchType;
    private final boolean isCatchAll;
    private final BlockStartInstr catchEntry;

    public ExHandler(ExceptionHandler handler, BlockStartInstr catchEntry) {
        startBci = handler.getStartBCI();
        endBci = handler.getEndBCI();
        catchType = handler.getCatchType();
        isCatchAll = handler.isCatchAll();
        this.catchEntry = catchEntry;
    }

    public boolean tryCover(int curBci) {
        return startBci >= curBci && curBci < endBci;
    }

    public boolean isCatchAll() {
        return isCatchAll;
    }

    public BlockStartInstr getCatchEntry() {
        return catchEntry;
    }
}
