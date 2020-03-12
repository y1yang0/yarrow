package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import jdk.vm.ci.meta.ExceptionHandler;
import jdk.vm.ci.meta.JavaKind;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BlockStartInstr extends Instruction {
    // For constructing control flow graph
    private int blockId;
    private int startBci;
    private int endBci;
    private List<BlockStartInstr> successor;
    private boolean mayThrowEx;
    private boolean loopHeader;
    private ExceptionHandler xhandler;

    // For instruction itself
    private BlockEndInstr blockEnd;
    private VmState state;

    public BlockStartInstr(int blockId, int bci) {
        super(new Value(JavaKind.Illegal));
        this.blockId = blockId;
        this.startBci = this.endBci = bci;
        this.successor = new ArrayList<>();
        this.mayThrowEx = false;
        this.loopHeader = false;
        this.blockEnd = null;
        this.state = null;
    }

    public int getEndBci() {
        return endBci;
    }

    public void setEndBci(int endBci) {
        this.endBci = endBci;
    }

    public int getStartBci() {
        return startBci;
    }

    public void setStartBci(int startBci) {
        this.startBci = startBci;
    }

    public void addSuccessor(BlockStartInstr block) {
        this.successor.add(block);
    }

    public List<BlockStartInstr> getSuccessor() {
        return successor;
    }

    public void removeSuccessor() {
        this.successor.clear();
    }

    public boolean isLoopHeader() {
        return loopHeader;
    }

    public void setLoopHeader(boolean loopHeader) {
        this.loopHeader = loopHeader;
    }

    public boolean isMayThrowEx() {
        return mayThrowEx;
    }

    public void setMayThrowEx(boolean mayThrowEx) {
        this.mayThrowEx = mayThrowEx;
    }

    public int getBlockId() {
        return blockId;
    }

    public ExceptionHandler getXhandler() {
        return xhandler;
    }

    public void setXhandler(ExceptionHandler xhandler) {
        this.xhandler = xhandler;
    }

    public void setBlockEnd(BlockEndInstr blockEnd) {
        this.blockEnd = blockEnd;
    }

    public void merge(VmState state){
        if(this.state==null){
            this.state = state.copy();
            if(this.isLoopHeader()){
                for(int i = 0; i<this.state.getStackSize(); i++){
                    this.state.createPhiForStack(this,i);
                }
                for(int i=0;i<this.state.getLocalSize();i++){
                    this.state.createPhiForLocal(this,i);
                }
            }
        }
    }

    public VmState getVmState() {
        return state;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlockStartInstr) {
            return ((BlockStartInstr) obj).blockId == this.blockId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockId);
    }

    @Override
    public String toString() {
        String successorString = successor.stream().map(
                b -> "#" + b.getBlockId() + " " + (b.xhandler != null ? "!" : "") + "[" + b.getStartBci() + "," + b.getEndBci() + "]"
        ).collect(Collectors.toList()).toString();

        return "#" +
                blockId +
                " " +
                (xhandler != null ? "!" : "") +
                "[" +
                startBci +
                "," +
                endBci +
                "]" +
                " => " +
                successorString;
    }
}
