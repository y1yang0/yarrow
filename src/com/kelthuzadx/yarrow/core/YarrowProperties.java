package com.kelthuzadx.yarrow.core;

import com.kelthuzadx.yarrow.util.Logger;
import com.kelthuzadx.yarrow.util.Mode;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;


public class YarrowProperties {
    static {
        Class<?>[] classes = YarrowProperties.class.getDeclaredClasses();
        Arrays.stream(classes).forEach(YarrowProperties::assignConfig);
    }

    static Optional<String> getYarrowProp(Class<?> klass, String key) {
        return Optional.ofNullable(System.getProperty("yarrow." + klass.getSimpleName() + "." + key));
    }

    private static void assignConfig(Class<?> klass) {
        Field[] fields = klass.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Optional<String> prop = YarrowProperties.getYarrowProp(klass, field.getName());
                if (prop.isEmpty()) {
                    continue;
                }
                if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                    if ("true".equals(prop.get())) {
                        field.setBoolean(null, true);
                    } else if ("false".equals(prop.get())) {
                        field.setBoolean(null, false);
                    } else {
                        Logger.log(Mode.Error, "Invalid yarrow property {} for -Dyarrow.{}.{}", prop.get(), klass.getSimpleName(), field.getName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Debug {
        public static boolean PrintCFG = false;
        public static boolean PrintIR = false;
        public static boolean PrintIRToFile = false;
        public static boolean PrintIdeal = false;
        public static boolean PrintLVN = false;
    }
}
