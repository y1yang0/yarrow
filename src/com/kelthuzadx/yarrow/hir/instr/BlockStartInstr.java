package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.CompilerErrors;
import com.kelthuzadx.yarrow.util.Constrain;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.ExceptionHandler;
import jdk.vm.ci.meta.JavaKind;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
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
        }else{
            Constrain.matchVmState(this.state,state);
            if(this.isLoopHeader()){
                for(int i=0;i<state.getLocalSize();i++){
                    if(state.get(i)==null || !state.get(i).isType(this.state.get(i).getType())){
                        CompilerErrors.bailOut();
                    }
                }
            }else{
                //TODO
            }
        }
    }

    public VmState getVmState() {
        return state;
    }

    public void iterateBytecode(Consumer<Instruction> closure){
        Instruction last = this;
        while (last!=null && last!=blockEnd){
            closure.accept(last);
            last = last.getNext();
        }
        if(last!=null && last==blockEnd){
            closure.accept(last);
        }
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

    public String toCFGString(){
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


    @Override
    public String toString() {
        return Logger.f("i{}: block_start",super.id);
    }
}
