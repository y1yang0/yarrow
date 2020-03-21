package com.kelthuzadx.yarrow.lir;

import com.kelthuzadx.yarrow.lir.instr.LirInstr;

import java.util.ArrayList;
import java.util.List;

public class LIR {
    private List<LirInstr> instructions;

    public LIR() {
        this.instructions = new ArrayList<>();
    }
}
