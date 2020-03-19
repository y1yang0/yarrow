package com.kelthuzadx.yarrow.hir.instr;

import com.kelthuzadx.yarrow.util.Logger;
import jdk.vm.ci.meta.JavaField;

import java.util.Objects;

public class LoadFieldInstr extends AccessFieldInstr {
    public LoadFieldInstr(Instruction object, int offset, JavaField field) {
        super(object, offset, field);
    }

    @Override
    public String toString() {
        return Logger.format("i{}: i{}.off+{} [{}.{}]", super.id, super.object.id,
                super.offset, field.getDeclaringClass().getUnqualifiedName(), field.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoadFieldInstr)) return false;
        var that = (LoadFieldInstr) o;
        return object.equals(that.object) && offset == that.offset && field.equals(that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, offset, field);
    }
}
