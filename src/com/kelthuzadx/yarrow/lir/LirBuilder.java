package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.hir.Hir;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.hir.instr.HirInstr;
import com.kelthuzadx.yarrow.optimize.Phase;

import java.util.ArrayDeque;
import java.util.HashSet;

public class LirBuilder implements Phase {
    private final Hir hir;
    private Lir lir;
    private HashSet<Integer> visit;
    private ArrayDeque<BlockStartInstr> workList;
    private LirGenerator generator;

    public LirBuilder(Hir hir) {
        this.hir = hir;
        this.lir = new Lir();
        this.generator = new LirGenerator(lir);
    }

    private void transformBlock(BlockStartInstr block) {
        HirInstr last = block;
        while (last != null && last != block.getBlockEnd()) {
            last.visit(generator);
            last = last.getNext();
        }
        if (last != null && last == block.getBlockEnd()) {
            last.visit(generator);
        }
    }

    @Override
    public Phase build() {
        visit = new HashSet<>();
        workList = new ArrayDeque<>();
        workList.add(hir.getEntryBlock());
        while (!workList.isEmpty()) {
            BlockStartInstr blockStart = workList.remove();
            if (!visit.contains(blockStart.getBlockId())) {
                visit.add(blockStart.getBlockId());
                transformBlock(blockStart);
                workList.addAll(blockStart.getBlockEnd().getSuccessor());
            }
        }
        lir.printLir();
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