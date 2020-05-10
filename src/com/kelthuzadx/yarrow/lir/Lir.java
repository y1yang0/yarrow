package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.hir.instr.BlockStartInstr;
import com.kelthuzadx.yarrow.lir.instr.LirInstr;
import com.kelthuzadx.yarrow.util.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Lir {
    private final HashMap<Integer, List<LirInstr>> instructions;
    private final ArrayList<BlockStartInstr> blocks;

    public Lir() {
        this.instructions = new HashMap<>();
        this.blocks = new ArrayList<>();
    }

    public ArrayList<BlockStartInstr> getBlocks() {
        return blocks;
    }

    public Set<Integer> getAllBlockId(){
        return instructions.keySet();
    }

    public List<LirInstr> getAllLirInstr(int blockStartId) {
        return instructions.get(blockStartId);
    }

    public void appendLirInstr(int blockStartId, LirInstr instr) {
        var instrs = instructions.get(blockStartId);
        if (instrs == null) {
            instructions.put(blockStartId, new ArrayList<>() {
                {
                    add(instr);
                }
            });
        } else {
            instrs.add(instr);
        }
    }

    public void appendBlock(BlockStartInstr instr){
        blocks.add(instr);
    }

    public void printLir() {
        Logger.logf("=====Phase: Low level IR=====>");
        instructions.forEach((id, list) -> {
            Logger.logf("L" + id + ":");
            for (LirInstr instr : list) {
                Logger.logf("{}", instr.toString());
            }
        });
    }
}
