package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.CompilerErrors;
import com.kelthuzadx.yarrow.util.Constraint;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.ExceptionHandler;
import jdk.vm.ci.meta.JavaKind;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BlockStartInstr extends StateInstr {
    // For constructing control flow graph
    private int blockId;
    private int startBci;
    private int endBci;
    // Successor of this block, when HIR construction accomplish, it will be cleared
    private List<BlockStartInstr> successor;
    private boolean mayThrowEx;
    private boolean loopHeader;
    private ExceptionHandler xhandler;

    // For instruction itself
    private BlockEndInstr blockEnd;
    private List<BlockStartInstr> predecessor;

    public BlockStartInstr(int blockId, int bci) {
        super(new Value(JavaKind.Illegal), null);
        this.blockId = blockId;
        this.startBci = this.endBci = bci;
        this.successor = new ArrayList<>();
        this.predecessor = new ArrayList<>();
        this.mayThrowEx = false;
        this.loopHeader = false;
        this.blockEnd = null;
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

    public List<BlockStartInstr> getPredecessor() {
        return predecessor;
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

    public BlockEndInstr getBlockEnd() {
        return blockEnd;
    }

    public void setBlockEnd(BlockEndInstr blockEnd) {
        // Connect to BlockEndInstr and remove BlockStartInstr's successors
        this.blockEnd = blockEnd;
        this.removeSuccessor();

        // Set predecessors of BlockStartInstr
        for(BlockStartInstr succ:blockEnd.getSuccessor()){
            succ.getPredecessor().add(this);
        }
        blockEnd.setBlockStart(this);
    }

    public void mergeVmState(VmState state) {
        if (getVmState() == null) {
            VmState newState = state.copy();
            if (this.isLoopHeader()) {
                for (int i = 0; i < newState.getStackSize(); i++) {
                    newState.createPhiForStack(this, i);
                }
                for (int i = 0; i < newState.getLocalSize(); i++) {
                    newState.createPhiForLocal(this, i);
                }
            }
            setVmState(newState);
        } else {
            Constraint.matchVmState(getVmState(), state);
            if (this.isLoopHeader()) {
                for (int i = 0; i < state.getLocalSize(); i++) {
                    if (state.get(i) == null || !state.get(i).isType(getVmState().get(i).getType())) {
                        CompilerErrors.bailOut();
                    }
                }
            } else {
                CompilerErrors.bailOut();
            }
        }
    }

    public void iterateBytecode(Consumer<Instruction> closure) {
        Instruction last = this;
        while (last != null && last != blockEnd) {
            closure.accept(last);
            last = last.getNext();
        }
        if (last != null && last == blockEnd) {
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

    public String toCFGString() {
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
        return Logger.format("i{}: block_start", super.id);
    }
}
