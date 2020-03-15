package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

import java.util.List;

public class LookupSwitchInstr extends BlockEndInstr {
    private Instruction index;
    private int[] key;

    public LookupSwitchInstr(VmState stateBefore, List<BlockStartInstr> successor, Instruction index, int[] key) {
        super(new Value(JavaKind.Illegal), stateBefore, successor);
        this.index = index;
        this.key = key;
    }

    @Override
    public String toString() {
        String caseStr = "";
        for (int i = 0; i < key.length; i++) {
            if (key.length - 1 != i) {
                caseStr += "#" + i + "->i" + getSuccessor().get(i).id + ",";
            } else {
                caseStr += "*" + i + "->i" + getSuccessor().get(i).id;
            }

        }
        return Logger.format("i{}: switch [{}]", super.id, caseStr);
    }
}