package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.hir.instr.Instruction;

import java.util.function.Consumer;

public class Walker {
    public static void walkBytecode(BlockStartInstr block, Consumer<Instruction> closure) {
        Instruction last = block;
        while (last != null && last != block.getBlockEnd()) {
            closure.accept(last);
            last = last.getNext();
        }
        if (last != null && last == block.getBlockEnd()) {
            closure.accept(last);
        }
    }
}
