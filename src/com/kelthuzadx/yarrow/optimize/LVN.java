package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.hir.HIR;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.hir.instr.Instruction;
import com.kelthuzadx.yarrow.phase.Phase;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class LVN implements Phase {
    private HIR hir;
    private Queue<BlockStartInstr> workList;
    private Set<Integer> visit;

    private LVN(HIR hir) {
        this.hir = hir;
        this.workList = new ArrayDeque<>();
        this.visit = new HashSet<>();
    }

    private void valueNumbering(BlockStartInstr block){
    }

    @Override
    public Phase build() {
        workList.add(hir.getEntryBlock());
        while (!workList.isEmpty()) {
            BlockStartInstr blockStart = workList.remove();
            if (!visit.contains(blockStart.id())) {
                visit.add(blockStart.id());
                valueNumbering(blockStart);
                workList.addAll(blockStart.getBlockEnd().getSuccessor());
            }
        }
        return null;
    }

    @Override
    public String name() {
        return "Local Value Numbering";
    }

    @Override
    public void log() {

    }
}
