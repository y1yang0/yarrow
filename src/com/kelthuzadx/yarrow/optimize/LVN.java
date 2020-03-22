package com.kelthuzadx.yarrow.optimize;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.instr.*;

import java.util.HashSet;
import java.util.Set;

public class LVN {
    private Set<HirInstr> valueSet;
    private HirInstr replacement;

    public LVN() {
        valueSet = new HashSet<>();
    }


    /**
     * Assignment instruction and monitor instruction may kill instructions in valueSet.
     *
     * @param instr instruction may kill other instructions in the value setz
     */
    private void killValue(HirInstr instr) {
        // long a = obj.field;
        // obj.field = ...
        // long b = obj.field;  prevent from LVN
        if (instr instanceof StoreFieldInstr) {
            var temp = (StoreFieldInstr) instr;
            var iter = valueSet.iterator();
            while (iter.hasNext()) {
                var i = iter.next();
                if (i instanceof LoadFieldInstr) {
                    var existing = (LoadFieldInstr) i;
                    if (existing.getOffset() == temp.getOffset() &&
                            existing.getObject().equals(temp.getObject()) &&
                            existing.getField().equals(temp.getField())) {
                        iter.remove();
                    }
                }
            }
        }
        // long a = arr[2];
        // arr[..] = ...
        // long b = arr[2];  prevent from LVN, kill the whole array
        else if (instr instanceof StoreIndexInstr) {
            var temp = (StoreIndexInstr) instr;
            var iter = valueSet.iterator();
            while (iter.hasNext()) {
                var i = iter.next();
                if (i instanceof LoadIndexInstr) {
                    var existing = (LoadIndexInstr) i;
                    if (existing.getArray().equals(temp.getArray()) &&
                            existing.getElementType() == temp.getElementType()) {
                        iter.remove();
                    }
                }
            }
        }
        // long a = obj.field;
        // invokestatic <class.method>
        // long b = obj.field; prevent from LVN, kill the whole memory
        else if (instr instanceof MonitorEnterInstr ||
                instr instanceof MonitorExitInstr ||
                instr instanceof CallInstr) {
            valueSet.removeIf(i -> i instanceof LoadIndexInstr || i instanceof LoadFieldInstr);
        }
    }

    public boolean hasReplacement(HirInstr instr) {
        for (HirInstr i : valueSet) {
            if (i.hashCode() == instr.hashCode() && i.equals(instr)) {
                YarrowError.guarantee(!(instr instanceof BlockEndInstr), "should never value numbering BlockEndInstr and its subclasses");
                replacement = i;
                killValue(instr);
                return true;
            }
        }
        valueSet.add(instr);
        killValue(instr);
        return false;
    }

    public HirInstr getReplacement() {
        return replacement;
    }
}
