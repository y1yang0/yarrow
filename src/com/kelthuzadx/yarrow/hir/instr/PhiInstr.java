package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PhiInstr extends HirInstr {
    private final int index; // negate number for stack, and positive number for local
    private final BlockStartInstr block;

    public PhiInstr(JavaKind type, int index, BlockStartInstr block) {
        super(type);
        this.index = index;
        this.block = block;
    }

    public BlockStartInstr getBlock() {
        return block;
    }

    public HirInstr operand(int i) {
        VmState state = block.getPredecessor().get(i).getBlockEnd().getVmState();
        if (state != null) {
            if (index >= 0) {
                return state.get(index);
            } else {
                return state.getStack().get(-(index + 1));
            }
        }
        return null;
    }

    public int operandCount() {
        return block.getPredecessor().size();
    }

    @Override
    public String toString() {
        String str = IntStream.range(0, operandCount())
                .mapToObj(i -> "i" + operand(i).id)
                .collect(Collectors.joining(","));

        return Logger.format("i{}: phi [{}]", super.id, str);
    }
}
