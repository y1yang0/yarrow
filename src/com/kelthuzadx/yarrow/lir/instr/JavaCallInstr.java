package com.kelthuzadx.yarrow.lir.instr;

import com.kelthuzadx.yarrow.lir.Mnemonic;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.AllocatableValue;
import jdk.vm.ci.meta.JavaMethod;

public class JavaCallInstr extends LirInstr {
    private final JavaMethod method;
    private final AllocatableValue receiver;
    private final AllocatableValue[] arguments;

    public JavaCallInstr(Mnemonic mnemonic, AllocatableValue result, JavaMethod method, AllocatableValue receiver, AllocatableValue[] arguments) {
        super(mnemonic, result);
        this.method = method;
        this.receiver = receiver;
        this.arguments = arguments;
    }

    public AllocatableValue getReceiver() {
        return receiver;
    }


    public AllocatableValue[] getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: {} {}", super.id, mnemonic.name().toLowerCase(), method.getName());
    }
}
