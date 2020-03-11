package com.kelthuzadx.yarrow.hir;


import java.util.Optional;

public class Value {
    private ValueType type;
    private Optional<Object> value;

    public Value(ValueType type){
        this.type = type;
        this.value = Optional.empty();
    }

    public Value(ValueType type, Object value){
        this.type = type;
        // to support special "null" value
        this.value = Optional.ofNullable(value);
    }

    public boolean isType(ValueType type){
        return this.type == type;
    }
}
