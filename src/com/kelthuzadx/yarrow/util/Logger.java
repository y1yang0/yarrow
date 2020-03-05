package com.kelthuzadx.yarrow.util;

import com.kelthuzadx.yarrow.core.YarrowError;

import java.text.SimpleDateFormat;

public class Logger {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("HH:mm:ss");

    public static void error(String msg) {
        throw new YarrowError(msg);
    }

    @SafeVarargs
    public static <T> void logf(String format, T... args) {
        System.out.println(replacePlaceHolder(format, args));
    }

    @SafeVarargs
    public static <T> void errorf(String format, T... args) {
        throw new YarrowError(replacePlaceHolder(format, args));
    }

    @SafeVarargs
    private static <T> String replacePlaceHolder(String format, T... args) {
        for (T arg : args) {
            format = format.replaceFirst("\\{.*?\\}", arg.toString());
        }
        return format;
    }
}
