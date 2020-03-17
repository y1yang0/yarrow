package com.kelthuzadx.yarrow.hir;

import com.kelthuzadx.yarrow.hir.instr.BlockEndInstr;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.hir.instr.Instruction;
import com.kelthuzadx.yarrow.util.Logger;
import com.kelthuzadx.yarrow.util.Mode;
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

    public void printHIR(boolean toFile) {
        if(!toFile){
            printHIR(new HashSet<>(), entry);
        }else{
            StringBuilder content = new StringBuilder();
            content.append("digraph G{\n");
            printHIRToFile(new HashSet<>(), entry, content);
            content.append("}");
            Logger.log(Mode.File, method.getDeclaringClass().getUnqualifiedName() + "_" +
                    method.getName() + "_phase2.dot", content.toString());
        }
    }

    private static void printHIR(Set<BlockStartInstr> visit, BlockStartInstr block) {
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

    private static void printHIRToFile(Set<BlockStartInstr> visit, BlockStartInstr block,StringBuilder content) {
        if (block == null || visit.contains(block)) {
            return;
        }
        // Block successors
        BlockEndInstr end = block.getBlockEnd();
        for(BlockStartInstr succ:end.getSuccessor()){
            content.append("\tB").append(block.getInstrId()).append("-> B").append(succ.getInstrId()).append(";\n");
        }
        // Block itself
        content.append("\tB").append(block.getInstrId()).append("[shape=record,label=\"");
        content.append("{ i").append(block.getInstrId()).append(" | ");
        Instruction start = block;
        String temp = "";
        while(start!=end){
            temp= start.toString();
            // escape "<" and ">" in graphviz record text
            temp = temp.replaceAll("<","\\\\<");
            temp = temp.replaceAll(">","\\\\>");
            content.append(temp).append("\\l"); //left align
            start = start.getNext();
        }
        temp= start.toString();
        temp = temp.replaceAll("<","\\\\<");
        temp = temp.replaceAll(">","\\\\>");
        content.append(temp).append("\\l");
        content.append("}\"];\n");


        visit.add(block);
        for (BlockStartInstr succ : block.getBlockEnd().getSuccessor()) {
            printHIRToFile(visit, succ,content);
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
