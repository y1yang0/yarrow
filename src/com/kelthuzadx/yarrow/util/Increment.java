package com.kelthuzadx.yarrow.util;

import java.util.HashMap;

public class Increment {
    private static HashMap<Class<?>, Integer> state = new HashMap<>();

    public static int next(Class<?> klass) {
        if (!state.containsKey(klass)) {
            state.put(klass, 0);
            return 0;
        }
        int oldValue = state.get(klass);
        state.put(klass, ++oldValue);
        return oldValue;
    }
}
