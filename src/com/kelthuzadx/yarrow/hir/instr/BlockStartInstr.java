package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.BlockFlag;
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
import java.util.stream.Collectors;

public class BlockStartInstr extends StateInstr {
    // For constructing control flow graph
    private int blockId;
    private int startBci;
    private int endBci;
    // Successor of this block, when HIR construction accomplish, it will be cleared
    private List<BlockStartInstr> successor;
    private boolean loopHeader;
    private ExceptionHandler exHandler;
    private BlockFlag flag;

    // For instruction itself
    private BlockEndInstr blockEnd;
    private List<BlockStartInstr> predecessor;

    public BlockStartInstr(int blockId, int bci) {
        super(new Value(JavaKind.Illegal), null);
        this.blockId = blockId;
        this.startBci = this.endBci = bci;
        this.successor = new ArrayList<>();
        this.predecessor = new ArrayList<>();
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

    public boolean hasSuccessor(BlockStartInstr block) {
        return successor.contains(block);
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

    public int getBlockId() {
        return blockId;
    }

    public void setExHandler(ExceptionHandler exHandler) {
        this.exHandler = exHandler;
    }

    public void setFlag(BlockFlag flag) {
        this.flag = flag;
    }

    public BlockEndInstr getBlockEnd() {
        return blockEnd;
    }

    public void setBlockEnd(BlockEndInstr blockEnd) {
        // Connect to BlockEndInstr and remove BlockStartInstr's successors
        this.blockEnd = blockEnd;
        this.removeSuccessor();

        // Set predecessors of BlockStartInstr
        for (BlockStartInstr succ : blockEnd.getSuccessor()) {
            succ.getPredecessor().add(this);
        }
        blockEnd.setBlockStart(this);
    }

    /**
     * If a block has more than one predecessor, PhiInstrc might be needed at
     * the beginning of this block. If I find different values of the same variable,
     * I will merge existing VmState(this.getVmState()) and new VmState.
     *
     * @param newState state of one of predecessors
     */
    public void mergeVmState(VmState newState) {
        if (getVmState() == null) {
            VmState state = newState.copy();
            if (this.isLoopHeader()) {
                for (int i = 0; i < state.getStackSize(); i++) {
                    if (state.getStack().get(i) != null) {
                        state.createPhiForStack(this, i);
                    }
                }
                for (int i = 0; i < state.getLocalSize(); i++) {
                    if (state.getLocal()[i] != null) {
                        state.createPhiForLocal(this, i);
                    }
                }
            }
            setVmState(state);
        } else {
            Constraint.matchVmState(getVmState(), newState);
            if (this.isLoopHeader()) {
                for (int i = 0; i < getVmState().getLocalSize(); i++) {
                    if (getVmState().get(i) != null) {
                        if (newState.getLocal()[i] == null ||
                                !newState.getLocal()[i].isType(getVmState().get(i).getType())) {
                            CompilerErrors.bailOut();
                        }
                    }
                }
            } else {
                for (int i = 0; i < getVmState().getStackSize(); i++) {
                    Instruction val = newState.getStack().get(i);
                    if (val != getVmState().getStack().get(i)) {
                        if (val instanceof PhiInstr) {
                            if (((PhiInstr) val).getBlock() != this) {
                                getVmState().createPhiForStack(this, i);
                            }
                        } else {
                            getVmState().createPhiForStack(this, i);
                        }
                    }
                }
                for (int i = 0; i < getVmState().getLocalSize(); i++) {
                    Instruction val = newState.getLocal()[i];
                    if (getVmState().getLocal()[i] != null) {
                        // If val exists and two local variable types match
                        if (val != null && Instruction.matchType(val, getVmState().getLocal()[i])) {
                            // if existing local variable is not PhiInstr OR
                            // if existing local variable is PhiInstr and it
                            // doesn't belong to this block
                            if (val != newState.getLocal()[i]) {
                                if (val instanceof PhiInstr) {
                                    if (((PhiInstr) val).getBlock() != this) {
                                        getVmState().createPhiForLocal(this, i);
                                    }
                                } else {
                                    getVmState().createPhiForLocal(this, i);
                                }
                            }
                        } else {
                            getVmState().getLocal()[i] = null;
                        }
                    }
                }
            }
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
                b -> "#" + b.getBlockId() + " " + (b.exHandler != null ? "!" : "") + "[" + b.getStartBci() + "," + b.getEndBci() + "]"
        ).collect(Collectors.toList()).toString();

        return "#" +
                blockId +
                " " +
                (exHandler != null ? "!" : "") +
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
