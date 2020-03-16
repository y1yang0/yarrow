package com.kelthuzadx.yarrow.hir;

import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.hir.instr.Instruction;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class HIR {
    private HotSpotResolvedJavaMethod method;
    private BlockStartInstr entry;
    private boolean writeFinal;
    private boolean writeVolatile;

    public HIR(HotSpotResolvedJavaMethod method, BlockStartInstr entry) {
        this.writeFinal = false;
        this.writeVolatile = false;
        this.entry = entry;
        this.method = method;
    }

    private static void iterateBytecodes(BlockStartInstr block, Consumer<Instruction> closure) {
        Instruction last = block;
        while (last != null && last != block.getBlockEnd()) {
            closure.accept(last);
            last = last.getNext();
        }
        if (last != null && last == block.getBlockEnd()) {
            closure.accept(last);
        }
    }

    public void printHIR() {
        printHIR(new HashSet<>(), entry);
    }

    private void printHIR(Set<BlockStartInstr> visit, BlockStartInstr block) {
        if (block == null || visit.contains(block)) {
            return;
        }
        Logger.logf("{}", block.getVmState().toString());
        iterateBytecodes(block, instr -> Logger.logf("{}", instr.toString()));
        Logger.logf("");
        visit.add(block);
        for (BlockStartInstr succ : block.getBlockEnd().getSuccessor()) {
            printHIR(visit, succ);
        }
    }

    public void setWriteFinal() {
        this.writeFinal = true;
    }

    public void setWriteVolatile() {
        this.writeVolatile = true;
    }

    public boolean isWriteFinal() {
        return writeFinal;
    }

    public boolean isWriteVolatile() {
        return writeVolatile;
    }
}
