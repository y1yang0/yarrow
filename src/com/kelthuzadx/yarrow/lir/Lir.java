package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.lir.instr.LirInstr;
import com.kelthuzadx.yarrow.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Lir {
    private final HashMap<Integer, List<LirInstr>> instructions;

    public Lir() {
        this.instructions = new HashMap<>();
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
