package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Cond;
import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;

import java.util.ArrayList;

public class IfInstr extends BlockEndInstr {
    private Instruction left;
    private Instruction right;
    private Cond cond;

    public IfInstr(VmState stateBefore, BlockStartInstr trueBlock, BlockStartInstr falseBlock, Instruction left, Instruction right, Cond cond) {
        super(new Value(JavaKind.Illegal), stateBefore, new ArrayList<>() {{
            add(trueBlock);
            add(falseBlock);
        }});
        this.left = left;
        this.right = right;
        this.cond = cond;
    }

    @Override
    public Instruction ideal() {
        if (left instanceof ConstantInstr && right instanceof ConstantInstr) {
            var ifState = getVmState();
            if (left == right && cond == Cond.EQ) {
                return new GotoInstr(ifState, getSuccessor().get(0));
            } else if (left == right && cond == Cond.NE) {
                return new GotoInstr(ifState, getSuccessor().get(1));
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
