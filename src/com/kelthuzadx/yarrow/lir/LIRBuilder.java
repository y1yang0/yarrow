package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.hir.HIR;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.hir.instr.HirInstruction;
import com.kelthuzadx.yarrow.phase.Phase;

import java.util.ArrayDeque;
import java.util.HashSet;

public class LIRBuilder implements Phase {
    private final HIR hir;
    private LIR lir;
    private HashSet<Integer> visit;
    private ArrayDeque<BlockStartInstr> workList;

    public LIRBuilder(HIR hir) {
        this.hir = hir;
        this.lir = new LIR();
    }

    private void transformBlock(BlockStartInstr block) {
        HirInstruction last = block;
        while (last != null && last != block.getBlockEnd()) {
            last.visit(new LirGenerator());
            last = last.getNext();
        }
        if (last != null && last == block.getBlockEnd()) {
            last.visit(new LirGenerator());
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
