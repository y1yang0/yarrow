package com.kelthuzadx.yarrow.util;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.VmState;

public class Constraint {

    public static void matchInt(int a, int b) {
        if (a != b) {
            throw new YarrowError("assertion failure");
        }
    }

    public static void matchVmState(VmState a, VmState b) {
        if (a.getStackSize() != b.getStackSize() ||
                a.getLocalSize() != b.getLocalSize() ||
                a.getLockSize() != b.getLockSize()) {
            throw new YarrowError("two VmState should be identical");
        }
        var stackA = a.getStack();
        var stackB = b.getStack();
        for (int i = 0; i < stackA.size(); i++) {
            if (!stackB.get(i).isType(stackA.get(i).getType())) {
                throw new YarrowError("two VmState should be identical");
            }
        }

        var lockA = a.getLock();
        var lockB = b.getLock();
        for (int i = 0; i < lockA.size(); i++) {
            if (lockA.get(i) != lockB.get(i)) {
                throw new YarrowError("two VmState should be identical");
            }
        }
    }

}
