package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.lir.instr.LirInstr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lir {
    private HashMap<Integer,List<LirInstr>>  instructions;

    public Lir() {
        this.instructions = new HashMap<>();
    }

    public void appendLirInstr(int blockStartId, LirInstr lirInstr){
        var lirInstrs = instructions.get(blockStartId);
        if(lirInstr==null){
            instructions.put(blockStartId,new ArrayList<>(){
                {add(lirInstr);}
            });
        }else{
            lirInstrs.add(lirInstr);
        }
    }
}
