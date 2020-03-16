package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.hir.Value;
import com.kelthuzadx.yarrow.hir.VmState;
import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaType;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MultiNewArrayInstr extends StateInstr {
    private JavaType klass;
    private Instruction[] dimenInstrs;

    public MultiNewArrayInstr(VmState stateBefore, JavaType klass, Instruction[] dimenInstrs) {
        super(new Value(JavaKind.Object), stateBefore);
        this.klass = klass;
        this.dimenInstrs = dimenInstrs;
    }

    @Override
    public String toString() {
        return Logger.format("i{}: new_multi {}{}", super.id, klass.getUnqualifiedName(),
                Arrays.stream(dimenInstrs).map(instr -> "[i" + instr.id + "]").reduce("",String::concat));
    }
}
