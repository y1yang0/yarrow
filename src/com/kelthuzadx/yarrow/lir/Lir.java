package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.lir.opcode.LirOpcode;
import com.kelthuzadx.yarrow.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lir {
    private HashMap<Integer, List<LirOpcode>> instructions;

    public Lir() {
        this.instructions = new HashMap<>();
    }

    public void appendLirInstr(int blockStartId, LirOpcode instr) {
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
            for (LirOpcode instr : list) {
                Logger.logf("{}", instr.toString());
            }
        });
    }
}
