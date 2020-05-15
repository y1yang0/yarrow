package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.instr.LirInstr;
import com.kelthuzadx.yarrow.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class Lir {
    private final HashMap<Integer, BlockStartInstr> blocks;

    public Lir() {
        this.blocks = new HashMap<>();
    }

    public BlockStartInstr fromBlock(int lirId) {
        for (BlockStartInstr block : new ArrayList<>(blocks.values())) {
            for (LirInstr instr : block.getLirInstrList()) {
                if (lirId == instr.getId()) {
                    return block;
                }
            }
        }
        return null;
    }

    public LirInstr fromInstr(int lirId) {
        for (BlockStartInstr block : new ArrayList<>(blocks.values())) {
            for (LirInstr instr : block.getLirInstrList()) {
                if (lirId == instr.getId()) {
                    return instr;
                }
            }
        }
        return null;
    }

    public ArrayList<BlockStartInstr> getBlocks() {
        return new ArrayList<>(blocks.values());
    }

    public BlockStartInstr getBlock(int id) {
        return blocks.get(id);
    }

    public void appendLirInstr(int blockStartId, LirInstr instr) {
        blocks.get(blockStartId).appendLirInstrList(instr);
    }

    public void appendBlock(BlockStartInstr instr) {
        blocks.put(instr.id(), instr);
    }

    public void printLir() {
        Logger.logf("=====Phase: Low level IR=====>");
        blocks.forEach((id, block) -> {
            for (LirInstr instr : block.getLirInstrList()) {
                Logger.logf("{}", instr.toString());
            }
        });
    }
}
