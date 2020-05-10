package com.kelthuzadx.yarrow.lir.regalloc;

import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.Lir;
import com.kelthuzadx.yarrow.lir.instr.LirInstr;
import com.kelthuzadx.yarrow.lir.operand.VirtualRegister;
import com.kelthuzadx.yarrow.optimize.Phase;
import com.kelthuzadx.yarrow.util.Logger;

import java.util.HashSet;

import static com.kelthuzadx.yarrow.core.YarrowProperties.Debug.TraceRegisterAllocation;


/**
 * Implementation of Linear Scan Register Allocation
 *
 * @author kelthuzadx
 */

public class RegisterAlloc implements Phase {
    private final Lir lir;

    public RegisterAlloc(Lir lir) {
        this.lir = lir;
    }

    @Override
    public Phase build() {
        numberingLirInstr();
        computeLocalLiveSet();
        computeGlobalLiveSet();
        return null;
    }

    private void numberingLirInstr() {
        /*
         * The number for the next operation is always incremented by 2, so only even number are used.
         * This simplifies many algorithms because there is always a free position between two operations
         * where a new operation, e.g. a spill move, can be inserted. Only when more than one operation
         * is inserted at a single position, the inserted operations must be ordered such that no register
         * is overwritten.
         */
        int opId = 0;
        for (BlockStartInstr block : lir.getBlocks()) {
            int id = block.id();
            for (int i = 0; i < lir.getAllLirInstr(id).size(); i++) {
                LirInstr instr = lir.getAllLirInstr(id).get(i);
                instr.resetId(opId);
                opId += 2;
            }
        }
    }

    private void computeLocalLiveSet() {
        /*
         * live_gen contains all operands that used in this block before they are defined, so they must be
         * defined somewhere in a predecessor. The set live_kill contains all operands that are defined in the
         * block, so a possible value of a predecessor is overwritten.
         */
        var visitor = new InstrStateVisitor();
        for (BlockStartInstr block : lir.getBlocks()) {

            int id = block.id();
            for (int i = 0; i < lir.getAllLirInstr(id).size(); i++) {
                visitor.reset();
                LirInstr instr = lir.getAllLirInstr(id).get(i);
                instr.visit(visitor);
                for (VirtualRegister value : visitor.getInput()) {
                    if (value.isVirtualRegister() &&
                            !block.liveKill().contains(value.getVirtualRegisterId())) {
                        block.liveGen().add(value.getVirtualRegisterId());
                    }
                }
                for (VirtualRegister value : visitor.getTemp()) {
                    if (value.isVirtualRegister() &&
                            !block.liveKill().contains(value.getVirtualRegisterId())) {
                        block.liveKill().add(value.getVirtualRegisterId());
                    }
                }
                for (VirtualRegister value : visitor.getOutput()) {
                    if (value.isVirtualRegister() &&
                            !block.liveKill().contains(value.getVirtualRegisterId())) {
                        block.liveKill().add(value.getVirtualRegisterId());

                    }
                }
            }
            if (TraceRegisterAllocation) {
                Logger.logf("=====Block {} live_gen {}, liven_kill {}",
                        String.valueOf(block.id()),
                        block.liveGen().toString(),
                        block.liveKill().toString());
            }
        }
    }

    private void computeGlobalLiveSet() {
        /*
         * The live_out set of a block is the union of the live_in sets of all successors. Because no
         * value can be generated at a control flow edge, all operands that live at the beginning of a
         * successors must also be live at the end of the current block. The live_in set is then calculated
         * from the live_out set using live_kill and live_gen.
         */
        boolean changed = false;
        int iterCount = 0;
        do {
            changed = false;
            var blocks = lir.getBlocks();
            for (int i = blocks.size() - 1; i >= 0; i--) {
                var block = blocks.get(i);
                var oldLiveOut = new HashSet<>(block.liveOut());
                for (BlockStartInstr succ : block.getBlockEnd().getSuccessor()) {
                    block.liveOut().addAll(succ.livenIn());
                }
                if (!oldLiveOut.equals(block.liveOut())) {
                    changed = true;
                }
                if (iterCount == 0 || changed) {
                    var temp = new HashSet<>(block.liveOut());
                    temp.removeAll(block.liveKill());
                    temp.addAll(block.liveGen());
                    block.livenIn().clear();
                    block.livenIn().addAll(temp);
                    if (TraceRegisterAllocation) {
                        Logger.logf("=====Block {} live_in {}, liven_out {}",
                                String.valueOf(block.id()),
                                block.livenIn().toString(),
                                block.liveOut().toString());
                    }
                }
            }
            iterCount++;
        } while (changed);
    }

    @Override
    public String name() {
        return "Register Allocation";
    }

    @Override
    public void log() {

    }
}
