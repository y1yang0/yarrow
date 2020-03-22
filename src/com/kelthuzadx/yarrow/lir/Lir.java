package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.lir.instr.LirInstr;
import com.kelthuzadx.yarrow.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lir {
    private HashMap<Integer, List<LirInstr>> instructions;

    public Lir() {
        this.instructions = new HashMap<>();
    }

    public void appendLirInstr(int blockStartId, LirInstr instr) {
        var lirInstrs = instructions.get(blockStartId);
        if (lirInstrs == null) {
            instructions.put(blockStartId, new ArrayList<>() {
                {
                    add(instr);
                }
            });
        } else {
            lirInstrs.add(instr);
        }
    }

    public void printLir() {
        Logger.logf("=====Phase: Low level IR=====>");
        instructions.forEach((id, list) -> {
            Logger.logf("#" + id);
            for (LirInstr instr : list) {
                Logger.logf("{}", instr.toString());
            }
        });
    }
}
