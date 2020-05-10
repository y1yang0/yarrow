package com.kelthuzadx.yarrow.lir.regalloc;

import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.Lir;
import com.kelthuzadx.yarrow.lir.instr.LirInstr;
import com.kelthuzadx.yarrow.optimize.Phase;

import java.util.Iterator;

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

    private void numberingLirInstr(){
        /*
         * The number for the next operation is always incremented by 2, so only even number are used.
         * This simplifies many algorithms because there is always a free position between two operations
         * where a new operation, e.g. a spill move, can be inserted. Only when more than one operation
         * is inserted at a single position, the inserted operations must be ordered such that no register
         * is overwritten.
         */
        int opId = 0;
        for(BlockStartInstr block:lir.getBlocks()){
            int id = block.id();
            for(int i = 0; i<lir.getAllLirInstr(id).size(); i++){
                LirInstr instr = lir.getAllLirInstr(id).get(i);
                instr.resetId(opId);
                opId+=2;
            }
        }
        lir.printLir();
    }

    private void computeLocalLiveSet(){
        var visitor = new InstrStateVisitor();
        for(BlockStartInstr block:lir.getBlocks()){

            int id = block.id();
            for(int i = 0; i<lir.getAllLirInstr(id).size(); i++){
                LirInstr instr = lir.getAllLirInstr(id).get(i);
                instr.visit(visitor);
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
