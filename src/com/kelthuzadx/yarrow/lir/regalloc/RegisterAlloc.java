package com.kelthuzadx.yarrow.lir.regalloc;

import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.Lir;
import com.kelthuzadx.yarrow.lir.instr.LirInstr;
import com.kelthuzadx.yarrow.lir.operand.VirtualRegister;
import com.kelthuzadx.yarrow.optimize.Phase;

import java.util.HashSet;
import java.util.Set;

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
        lir.printLir();
    }

    private void computeLocalLiveSet() {
        /*
         * live_gen contains all operands that used in this block before they are defined, so they must be
         * defined somewhere in a predecessor. The set live_kill contains all operands that are defined in the
         * block, so a possible value of a predecessor is overwritten.
         */
        var visitor = new InstrStateVisitor();
        for (BlockStartInstr block : lir.getBlocks()) {
            Set<Integer> liveGen = new HashSet<>();
            Set<Integer> liveKill = new HashSet<>();

            int id = block.id();
            for (int i = 0; i < lir.getAllLirInstr(id).size(); i++) {
                visitor.reset();
                LirInstr instr = lir.getAllLirInstr(id).get(i);
                instr.visit(visitor);
                for (VirtualRegister value : visitor.getInput()) {
                    if (!liveKill.contains(value.getVirtualRegisterId())) {
                        liveGen.add(value.getVirtualRegisterId());
                    }
                }
                for (VirtualRegister value : visitor.getTemp()) {
                    liveKill.add(value.getVirtualRegisterId());
                }
                for (VirtualRegister value : visitor.getOutput()) {
                    liveKill.add(value.getVirtualRegisterId());
                }
            }
            block.setLiveGen(liveGen);
            block.setLiveKill(liveKill);
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
