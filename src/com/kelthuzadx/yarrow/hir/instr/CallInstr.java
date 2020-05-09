package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.bytecode.Bytecode;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaMethod;
import jdk.vm.ci.meta.Signature;


public class CallInstr extends StateInstr {
    private final HirInstr[] args;
    private final JavaMethod method;
    private final Signature signature;
    private final int opcode;
    private HirInstr receiver;

    public CallInstr(JavaKind type, VmState stateBefore, HirInstr receiver, HirInstr[] args, JavaMethod method, Signature signature, int opcode) {
        super(type, stateBefore);
        this.args = args;
        this.method = method;
        this.signature = signature;
        this.opcode = opcode;
    }

    public JavaMethod getMethod() {
        return method;
    }

    public int getOpcode() {
        return opcode;
    }

    public boolean hasReceiver() {
        return receiver != null;
    }

    public HirInstr getReceiver() {
        return receiver;
    }

    public HirInstr getArguments(int index) {
        return args[index];
    }

    public int argumentCount() {
        return args.length;
    }

    public Signature getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: {} {}.{}", super.id, Bytecode.forName(opcode), method.getDeclaringClass().getUnqualifiedName(), method.getName());
    }
}
