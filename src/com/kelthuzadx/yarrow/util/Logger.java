package com.kelthuzadx.yarrow.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Logger<M extends Mode> {

    @SafeVarargs
    public static <T> String format(String format, T... args) {
        return replacePlaceHolder(format, args);
    }

    @SafeVarargs
    public static <T> void logf(String format, T... args) {
        System.out.println(replacePlaceHolder(format, args));
    }

    @SafeVarargs
    public static <T> void log(Mode mode, T... args) {
        String content;
        if (mode == Mode.Console) {
            content = replacePlaceHolder((String) args[0], Arrays.copyOfRange(args, 1, args.length));
            System.out.println(content);
        } else if (mode == Mode.Error) {
            content = replacePlaceHolder((String) args[0], Arrays.copyOfRange(args, 1, args.length));
            System.err.println(content);
            System.exit(-1);
        } else if (mode == Mode.File && args.length >= 2) {
            Path path = Paths.get((String) args[0]);
            content = replacePlaceHolder((String) args[1], Arrays.copyOfRange(args, 2, args.length));
            try (BufferedWriter w = Files.newBufferedWriter(path)) {
                w.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("illegal arguments for log, neither missing args or invalid mode");
        }
    }

    @SafeVarargs
    private static <T> String replacePlaceHolder(String format, T... args) {
        for (T arg : args) {
            format = format.replaceFirst("\\{.*?\\}", arg.toString());
        }

        return format;
    }
}
