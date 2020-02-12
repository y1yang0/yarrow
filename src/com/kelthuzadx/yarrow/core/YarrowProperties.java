package com.kelthuzadx.yarrow.core;

import com.kelthuzadx.yarrow.util.Logger;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import static com.kelthuzadx.yarrow.core.YarrowProperties.getYarrowProp;



public class YarrowProperties {
    public static class Debug{
        public static boolean TraceBytecode = false;
        public static boolean PrintCFG = false;
    }

    static{
        Class<?>[] classes = YarrowProperties.class.getDeclaredClasses();
        Arrays.stream(classes).forEach(YarrowProperties::assignConfig);
    }


    static Optional<String> getYarrowProp(Class<?> klass, String key){
        return Optional.ofNullable(System.getProperty("yarrow."+klass.getSimpleName()+"."+key));
    }

    private static void assignConfig(Class<?> klass){
        Field[] fields = klass.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Optional<String> prop = YarrowProperties.getYarrowProp(klass,field.getName());
                if (prop.isEmpty()) {
                    continue;
                }
                if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                    if ("true".equals(prop.get())) {
                        field.setBoolean(null, true);
                    } else if ("false".equals(prop.get())) {
                        field.setBoolean(null, false);
                    } else {
                        Logger.error("Invalid yarrow property " + prop.get() + " for -Dyarrow." +klass.getSimpleName()+"." +field.getName());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
