package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import jdk.vm.ci.meta.JavaMethod;
import jdk.vm.ci.meta.Signature;


public class CallInstr extends StateInstr{
    private Instruction[] args;
    private JavaMethod target;
    private Signature signature;
    private int opcode;

    public CallInstr(Value value, VmState stateBefore, Instruction[] args, JavaMethod target, Signature signature, int opcode) {
        super(value, stateBefore);
        this.args = args;
        this.target = target;
        this.signature = signature;
        this.opcode = opcode;
    }
}
