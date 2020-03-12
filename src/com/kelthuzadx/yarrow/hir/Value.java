package com.kelthuzadx.yarrow.hir;


import jdk.vm.ci.meta.JavaKind;

import java.util.Optional;

public class Value {
    private JavaKind type;
    private Optional<Object> value;

    public Value(JavaKind type) {
        this.type = type;
        this.value = Optional.empty();
    }

    public Value(JavaKind type, Object value) {
        this.type = type;
        // to support special "null" value
        this.value = Optional.ofNullable(value);
    }

    public boolean isType(JavaKind type) {
        return this.type == type;
    }

    public JavaKind getType() {
        return type;
    }

    public Optional<Object> getValue() {
        return value;
    }
}
