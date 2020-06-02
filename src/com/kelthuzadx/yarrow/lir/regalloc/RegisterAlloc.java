package com.kelthuzadx.yarrow.lir.regalloc;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.Lir;
import com.kelthuzadx.yarrow.lir.instr.LirInstr;
import com.kelthuzadx.yarrow.lir.operand.XRegister;
import com.kelthuzadx.yarrow.optimize.Phase;
import com.kelthuzadx.yarrow.util.Logger;

import java.util.HashMap;
import java.util.HashSet;

import static com.kelthuzadx.yarrow.core.YarrowProperties.Debug.TraceRegisterAllocation;


/**
 * Implementation of Linear Scan Register Allocation
 *
 * @author kelthuzadx
 */

public class RegisterAlloc implements Phase {
    private final Lir lir;
    private final HashMap<Integer, Interval> intervals;

    public RegisterAlloc(Lir lir) {
        this.lir = lir;
        this.intervals = new HashMap<>();
    }

    @Override
    public Phase build() {
        numberingLirInstr();
        computeLocalLiveSet();
        computeGlobalLiveSet();
        buildInterval();
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
            for (int i = 0; i < lir.getBlock(id).getLirInstrList().size(); i++) {
                LirInstr instr = lir.getBlock(id).getLirInstrList().get(i);
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
            for (int i = 0; i < lir.getBlock(id).getLirInstrList().size(); i++) {
                visitor.reset();
                LirInstr instr = lir.getBlock(id).getLirInstrList().get(i);
                instr.visit(visitor);
                for (XRegister value : visitor.getInput()) {
                    if (value.isVirtualRegister() &&
                            !block.liveKill().contains(value.getVirtualRegisterId())) {
                        block.liveGen().add(value.getVirtualRegisterId());
                    }
                }
                for (XRegister value : visitor.getTemp()) {
                    if (value.isVirtualRegister()) {
                        block.liveKill().add(value.getVirtualRegisterId());
                    }
                }
                for (XRegister value : visitor.getOutput()) {
                    if (value.isVirtualRegister()) {
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
        boolean changed;
        int iterCount = 0;
        do {
            changed = false;
            var blocks = lir.getBlocks();
            for (int i = blocks.size() - 1; i >= 0; i--) {
                var block = lir.getBlocks().get(i);
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

    private void buildInterval() {
        /*
         * After the data flow analysis, all information necessary to construct accurate live ranges
         * and use positions are available. Again, all operaitons of all blocks are iterated, but this
         * time in reverse order.
         *
         * Before the operation are processed, the live_out set of the block is used to generate the
         * ranges that must last until the operation of the block. At first, the entire range of the
         * block is added - this is necessary if the operand does not occur in any operation of the
         * block. If the operand is defined in the block, then the range is shortened to the definition
         * position later.
         */
        for (int i = lir.getBlocks().size() - 1; i >= 0; i--) {
            var block = lir.getBlocks().get(i);
            var instrList = block.getLirInstrList();
            int blockFrom = instrList.get(0).getId();
            int blockTo = instrList.get(instrList.size() - 1).getId() + 2;
            if (blockFrom > blockTo) {
                YarrowError.shouldNotReachHere();
            }
            // Logger.logf("B{}:",block.getBlockId());
            for (Integer id : block.liveOut()) {
                var interval = new Interval();
                interval.addRange(blockFrom, blockTo);
                intervals.put(id, interval);

                //System.out.println("\tv" + id + ":" + interval);
            }

            // Reverse order
            for (int k = instrList.size() - 1; k >= 0; k--) {
                InstrStateVisitor visitor = new InstrStateVisitor();
                instrList.get(k).visit(visitor);

                if (visitor.hasCall()) {
                    //TODO: destroy all physical registers
                }

                for (XRegister value : visitor.getInput()) {
                }
                for (XRegister value : visitor.getTemp()) {
                }
                for (XRegister value : visitor.getOutput()) {
                }
            }
        }
    }

    @Override
    public String name() {
        return "Register Allocation";
    }

    @Override
    public void log() {

    }
}
