package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.core.YarrowError;
import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

import java.util.ArrayList;

public class IfInstr extends BlockEndInstr {
    private HirInstruction left;
    private HirInstruction right;
    private Cond cond;

    public IfInstr(VmState stateBefore, BlockStartInstr trueBlock, BlockStartInstr falseBlock, HirInstruction left, HirInstruction right, Cond cond) {
        super(new Value(JavaKind.Illegal), stateBefore, new ArrayList<>() {{
            add(trueBlock);
            add(falseBlock);
        }});
        this.left = left;
        this.right = right;
        this.cond = cond;
    }

    @Override
    public HirInstruction ideal() {
        if (left instanceof ConstantInstr && right instanceof ConstantInstr) {
            var ifState = getVmState();
            // if i1 == i1 then i2 else i3
            // if i2 != i2 then i3 else i4
            if (left == right && cond == Cond.EQ) {
                return new GotoInstr(ifState, getSuccessor().get(0));
            } else if (left == right && cond == Cond.NE) {
                return new GotoInstr(ifState, getSuccessor().get(1));
            }

            // i1: 1
            // i2: 23
            // if i1 == i2 then i3 else i4
            if (left.isType(JavaKind.Int) && right.isType(JavaKind.Int)) {
                int x = left.value();
                int y = right.value();
                if (Cond.EQ == cond) {
                    if (x == y) {
                        return new GotoInstr(ifState, getSuccessor().get(0));
                    } else {
                        return new GotoInstr(ifState, getSuccessor().get(1));
                    }
                } else if (Cond.NE == cond) {
                    if (x != y) {
                        return new GotoInstr(ifState, getSuccessor().get(0));
                    } else {
                        return new GotoInstr(ifState, getSuccessor().get(1));
                    }
                } else if (Cond.GE == cond) {
                    if (x >= y) {
                        return new GotoInstr(ifState, getSuccessor().get(0));
                    } else {
                        return new GotoInstr(ifState, getSuccessor().get(1));
                    }
                } else if (Cond.GT == cond) {
                    if (x > y) {
                        return new GotoInstr(ifState, getSuccessor().get(0));
                    } else {
                        return new GotoInstr(ifState, getSuccessor().get(1));
                    }
                } else if (Cond.LE == cond) {
                    if (x <= y) {
                        return new GotoInstr(ifState, getSuccessor().get(0));
                    } else {
                        return new GotoInstr(ifState, getSuccessor().get(1));
                    }
                } else if (Cond.LT == cond) {
                    if (x < y) {
                        return new GotoInstr(ifState, getSuccessor().get(0));
                    } else {
                        return new GotoInstr(ifState, getSuccessor().get(1));
                    }
                } else {
                    YarrowError.shouldNotReachHere();
                }
            }
        }
        return this;
    }

    @Override
    public String toString() {
        String op = "";
        switch (cond) {
            case EQ:
                op = "==";
                break;
            case NE:
                op = "!=";
                break;
            case LT:
                op = "<";
                break;
            case GE:
                op = ">=";
                break;
            case GT:
                op = ">";
                break;
            case LE:
                op = "<=";
                break;
        }
        return Logger.format("i{}: if i{} {} i{} then i{} else i{}",
                super.id, left.id, op, right.id,
                super.getSuccessor().get(0).id,
                super.getSuccessor().get(1).id);
    }
}
